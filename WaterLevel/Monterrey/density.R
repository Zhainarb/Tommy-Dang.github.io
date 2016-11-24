library(RJSONIO)
library(spatstat)
library(rgdal)
library(ggplot2)
library(stringr)
library(splancs)


##To download the data go to this address:
##http://gruporeforma.elnorte.com/libre/offlines/mty/mapas/mapadelcrimen2011.htm
##and type this command into chrome inspector to get a json dump
##JSON.stringify(officeLayer)
json_file <- "monterrey2011.json"
places <- fromJSON(paste(readLines(json_file), collapse=""))

##Prepare the shapefile
##Go here if you need a shapefile of the municipalities of Mexico:
##http://blog.diegovalle.net/2013/02/download-shapefiles-of-mexico.html

mun.shp <- readOGR("maps", "MUNICIPIOS-60")
##Transform the projection to Google Mercator
epsg3857String <- CRS("+proj=longlat +ellps=WGS84")  
mun.shp <- spTransform(mun.shp,  epsg3857String)

##Subset the Monterrey Metro Area
mun.shp <- mun.shp[mun.shp$CVE_ENT == 19 & mun.shp$CVE_MUN %in% c("006", "009",
                                                                  "018", "019", "021", "026", "031", "039",
                                                                  "045", "046", "048", "049"),]
##Assing a unique identifier to each municipality based
##state code and municipality code
mun.shp$id <- str_c(mun.shp$CVE_ENT, mun.shp$CVE_MUN)
mun.f <- fortify(mun.shp, region = "id")




## Extract the lat,long, number of dead, date and the story text from
## the json file

len <- length(places[[1]]$places)
coord <- data.frame(lat = numeric(),
                    long = numeric(),
                    dead= numeric(),
                    date = character(),
                    text = character())
for(i in 1:len) {
  lat <- places[[1]]$places[[i]]$posn[1]
  
  long <- places[[1]]$places[[i]]$posn[2]
  
  dead <- as.numeric(gsub(".*<b>([0-9]+)</b> Muerto.*",
                          "\\1",
                          places[[1]]$places[[i]]$herramienta[3]))
  date <- gsub(".*<td class='arngo10'>([0-9]+[ a-zA-Z0-9-]*) *</td>.*",
               "\\1",
               places[[1]]$places[[i]]$herramienta[3])
  text <- places[[1]]$places[[i]]$herramienta[3]
  coord= rbind(coord, data.frame(lat = lat, long = long, dead = dead,
                                 date = date, text = text))
}

## Translate the month names to english
monthes <- c("Enero" , "Febrero", "Marzo", "Abril", "Mayo", "Junio",
             "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre",
             "Diciembre")
monthen <- c("January", "February", "March", "April", "May", "June",
             "July", "August", "September", "October", "November",
             "December")
for(i in 1:12)
  coord$date <- gsub(str_c(" de ", monthes[i]),
                     str_c("-", monthen[i], "-2011"),
                     coord$date)
coord$date <- as.Date(coord$date, format("%d-%B-%Y"))


## the data is sometimes wrong, so manually correct
coord[coord$lat == 25.686881 & coord$long == -100.325432,]$dead <- 2
coord[coord$lat == 25.730787 & coord$long == -100.291464,]$dead <- 1
coord[coord$lat == 25.79261 & coord$long == -100.304215,]$dead <- 1
coord[coord$lat == 25.803929 & coord$long == -100.352596,]$dead <- 1
coord[coord$lat == 25.790612 & coord$long == -100.252709,]$dead <- 4
coord[coord$lat == 25.758414 & coord$long == -100.286885,]$dead <- 2
coord[coord$lat == 25.792876 & coord$long ==  -100.177388,]$dead <- 4
coord[coord$lat == 25.616506 & coord$long ==  -100.278697,]$dead <- 2
coord[coord$lat == 25.616132 & coord$long == -100.270704,]$dead <- 1

coord[coord$lat ==  25.654553 & coord$long == -100.310783,]$dead <- 5
coord[coord$lat == 25.674297 & coord$long == -100.437074,]$dead <- 1
coord[coord$lat == 25.665048 & coord$long == -100.22548,]$dead <- 3

coord[coord$lat == 25.763103 & coord$long == -100.127678,]$dead <- 1
coord[coord$lat == 25.426964 & coord$long == -100.151683,]$dead <- 2
coord[coord$lat ==  25.429943 & coord$long == -100.147934,]$dead <- 1

coord[coord$lat ==  25.517226 & coord$long == -100.220733,]$dead <- 1
coord[coord$lat == 25.506503 & coord$long == -100.193081,]$dead <- 1

coord[coord$lat == 25.587607 & coord$long == -100.253858,]$dead <- 1
coord[coord$lat == 25.577682 & coord$long == -100.243731,]$dead <- 1

## Add Casino Royale (52 deaths) and Sabino Gordo (21 deaths)
royale <- data.frame(lat = 25.674330, long = -100.354893,
                     date = as.Date("2011-08-25"),
                     dead = 52, text = "")
sabino <- data.frame(lat = 25.683119, long = -100.321902,
                     date = as.Date("2011-07-09"),
                     dead = 21, text = "")




## Now that we have clean data its time to compute the 2d density of homicides
spatstat.options(checkpolygons = FALSE)
win = as(mun.shp, "owin")
spatstat.options(npixel = 500) #avoid the mario bros pixelated look

mx.points <- as.ppp(ppp(
  coord$long,
  coord$lat,
  window = win  ##The extenct of the Monterrey metro area
))

crds <- coordinates(as.points(mx.points))
##  Yeah, I know I should use the shapefile or better yet
## polygons for the urban localities of Monterrey, but
## I'm lazy and used the convex hull of the murder locations
poly <- crds[chull(mx.points),]

## Minimize the mse for a kernel smoothing 
mserw <- mse2d(as.points(mx.points), poly=poly, nsmse=100, range=1)
bw <- mserw$h[which.min(mserw$mse)] ## Bandwidth=.01

## Now that we know the value which minimizes the mse let's
## do the kernel smoothing
img <- kernel2d(as.points(mx.points), poly, h0 = bw, 
                nx = 100, ny = 100)
## We want the contour lines for the d3 vis
img.contours <- ContourLines2SLDF(contourLines(img))

## Visually inspect the result
image(img)
## Write a shapefile that we can then convert to topojson
writeOGR(img.contours, "d3/contours.shp", "contours", "ESRI Shapefile")