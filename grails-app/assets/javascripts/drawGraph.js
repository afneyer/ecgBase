		    function drawGraph( graphColumns, graphData, graphOptions ) {

				var data = new google.visualization.DataTable();
			
				// alert("here is passed data\n"+graphData);
			    // alert("here are passed graph options\n" + graphOptions );
			    
			    // alert ("before Columns\n" + 'var gColumns = ' + graphColumns );
			    eval('var gColumns = ' + graphColumns);
			    // alert("here is gColumns\n" + gColumns );
				for (var i = 0; i < gColumns.length; i++) {
				 	 // alert(gColumns[i]);
                     data.addColumn( gColumns[i][0], gColumns[i][1] );
                }          
			    
				eval('var gData = ' + graphData);
			    data.addRows(gData);
			
				eval('var options = ' + graphOptions );
	
				var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
			
				chart.draw(data, options);
			}	