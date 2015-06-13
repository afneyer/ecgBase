function drawGraph(graphData) {

	var data = new google.visualization.DataTable();
	data.addColumn('number', 'X');
	data.addColumn('number', 'Cats');

	// alert("here is passed data\n"+graphData);

	eval('var gData = ' + graphData);

	data.addRows(gData);

	var options = {
		hAxis : {
			title : 'Time'
		},
		vAxis : {
			title : 'Popularity'
		}
	};

	var chart = new google.visualization.LineChart(document
			.getElementById('chart_div'));

	chart.draw(data, options);
}