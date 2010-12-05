$(document).ready(function() {
	
	var formatDate = function(d) {
		var dd = function(x) { return x < 10 ? '0' + x : x; };
		return d.getFullYear() + "-" + dd(d.getMonth() + 1) + "-" + dd(d.getDate()) +
			" " + dd(d.getHours()) + ":" + dd(d.getMinutes()) + ":" + dd(d.getSeconds());
	};
	
	$('textarea').keyup(function() {
		$('span.count').html(this.value.length);
	});
	$.getJSON('msg/latest', function(data) {
		for(var i = data.length - 1; i >= 0; i--) {
			var timestamp = new Date(+data[i].timestamp);
			$('#messages tbody').prepend('<tr>' +
				'<td>' + (data[i].incoming ? '&#10525;' : '&#10526;') + '</td>' +
				'<td>' + data[i].address + '</td>' +
				'<td>' + data[i].body + '</td>' +
				'<td><nobr>' + formatDate(timestamp) + '</nobr></td></tr>'); 
		}
	});
});
