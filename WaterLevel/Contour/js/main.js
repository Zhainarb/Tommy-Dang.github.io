var data2D = [];
      
d3.tsv("data/ascii_2013.csv", function(error, data_) { 
  for (var i=0;i<data_.length-19;i++){
    var a=[];
    for (var key in data_[i]){
      var v = +data_[i][key];
    // if (v<0)
     //   v=null;
     //   v=-10;
      a.push(v);
    }
    data2D.unshift(a);
  }
  console.log(data_.length);

  var data = [ {
    z: data2D,  
 //  zsmooth: 'best',
  type: 'contour',
  //colorscale: 'Jet',
  showscale: true,
  autocontour: false,
  contours: {
    start: 20,
    end: 900,
    size: 80
  },
  colorscale: [[0, 'rgba(255,255,255,0)'], [0.1, 'rgba(31,120,180,255)'], [0.25, 'rgb(178,200,138)'], [0.4, 'rgb(151,160,44)'], [0.55, 'rgb(251,154,153)'], [0.7, 'rgb(227,26,28)'] , [0.85, 'rgb(150,0,0)'] , [1, 'rgb(110,0,0)']],
  xaxis: 'x3',
  yaxis: 'y3'
   }];

  var layout = {
    title: 'Water level Contours',
    width: 850,
    height: 1000,
  };

  Plotly.newPlot('myDiv', data, layout);
});

