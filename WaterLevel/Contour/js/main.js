margin_ = {l: 50, r: 50, b: 100, t: 100, pad: 0};
var eventData_;
var data2D = [];
      
d3.tsv("data/ascii_2013.csv", function(error, data_) { 


  getLocation();


  for (var i=0;i<data_.length-19;i++){
    var a=[];
    for (var key in data_[i]){
      var v = +data_[i][key];
    // if (v<0)
     //   v=null;
     //   v=-10;
      a.push(v);
    }
    data2D.push(a);
  }
  console.log(data_.length);

  var data = [ {
      z: data2D,  
     //  zsmooth: 'best',
      type: 'contour',
      showscale: true,
      autocontour: false,
      contours: {
        start: 20,
        end: 700,
        size: 60
      },
      colorscale: [[0, 'rgba(255, 255, 255,0)'],[0.1, 'rgba(250,200,160,1)'], [0.2, 'rgba(200,150,130,255)'], [0.3, 'rgb(160,160,80)'], [0.4, 'rgb(0,120,160)'], [0.7, 'rgb(0,60,120)'] , [1, 'rgb(0,0,60)']]
     },
    {
      x: [200],
      y: [25],
      fillcolor: 'rgb(0, 128, 191)',
      line: {
                'color': 'rgb(193, 0, 0,)',
            },
      mode: 'circle'
    }

   ];

  var layout = {
    title: 'Saturated Thickness of Ogallala Aquifier in 2013',
    width: 850,
    height: 1000,

    xaxis: {
      side: 'top' 
    },

    yaxis: {
      autorange: 'reversed'
    },
  
    shapes: [
      //line vertical
      {
        type: 'circle',
        x0: 11,
        y0: 10,
        x1: 20,
        y1: 20,
        fillcolor: 'rgb(55, 128, 191)'
      }
    ]
  };

  Plotly.newPlot('myDiv', data, layout);

  var layout = {
    title: 'Saturated Thickness of Ogallala Aquifier in 2013',
    width: 850,
    height: 1000,
    margin: margin_,
    xaxis: {
      side: 'top' 
    },

    yaxis: {
      autorange: 'reversed'
    },

  
    shapes: [

      //line vertical

      {
        type: 'circle',
        x0: 11,
        y0: 10,
        x1: 20,
        y1: 20,
        fillcolor: 'rgb(55, 128, 191)'
      }
    ]
  };
  Plotly.newPlot('myDiv', data, layout);


  myDiv.on('plotly_relayout',
    function(eventdata){  
      eventData_ = eventdata;
        alert( 'ZOOM!' + '\n\n' +
            'Event data:' + '\n' + 
             JSON.stringify(eventdata) + '\n\n' +
            'x-axis start:' + eventdata['xaxis.range[0]'] + '\n' +
            'x-axis end:' + eventdata['xaxis.range[1]'] );


      /*  var xx = 200;
        var yy = 200;
        if (eventData_){
          xx = (xx-eventData_['xaxis.range[0]'])*850/(eventdata['xaxis.range[1]']-eventdata['xaxis.range[0]']) ;
          yy = (yy-eventData_['yaxis.range[0]'])*100/(eventdata['yaxis.range[1]']-eventdata['yaxis.range[0]']);
        }

        console.log(eventData_+" xx="+xx+" yy="+yy);
        d3.selectAll(".shapelayer").append("circle")
          .attr("stroke", "#f00")
          .attr("stroke-width", 2)
          .attr("fill", "#f00")
          .attr("cx", margin_.l+xx)
          .attr("cy", margin_.t+yy)
          .attr("r", 7);*/

          //d3.selectAll(".shapelayer").selectAll("path").style("stroke","#f00");
          d3.selectAll(".point").attr("class","location");
          d3.selectAll(".location").style("stroke","#0f0").style("stroke-width",4);
    });

  
  setTimeout(alertFunc, 3000);


function alertFunc() {
    //alert("Hello!");
    d3.selectAll(".location").style("stroke-width",1);

    d3.selectAll(".point").attr("class","location");
    d3.selectAll(".location").transition()
    .style("stroke","#00f").style("stroke-width",10);
    setTimeout(alertFunc, 500);
}
  
});

