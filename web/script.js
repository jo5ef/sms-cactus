$(document).ready(function() {
	$.getJSON('msg/latest', function(data) {
		for(var i = data.length - 1; i >= 0; i--) {
			var timestamp = new Date(+data[i].timestamp);
			$('#messages tbody').prepend('<tr>' +
				'<td>' + data[i].id + '</td>' +
				'<td>' + data[i].address + '</td>' +
				'<td>' + data[i].body + '</td>' +
				'<td>' + timestamp + '</td>' +
				'<td>' + data[i].incoming + '</td></tr>'); 
		}
	});
});
