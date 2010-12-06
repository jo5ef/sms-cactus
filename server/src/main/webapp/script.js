$(document).ready(function() {
	
	var formatDate = function(d) {
		var dd = function(x) { return x < 10 ? '0' + x : x; };
		return d.getFullYear() + "-" + dd(d.getMonth() + 1) + "-" + dd(d.getDate()) +
			" " + dd(d.getHours()) + ":" + dd(d.getMinutes()) + ":" + dd(d.getSeconds());
	};
	
	$('textarea').keyup(function() {
		$('span.count').html(this.value.length);
	});
	
	var lastid = 0;
	
	var addSms = function(sms) {
	
		var init = lastid == 0;		
	
		for(var i = sms.length - 1; i >= 0; i--) {
			var timestamp = new Date(+sms[i].timestamp);
			$('#messages tbody').prepend('<tr' + (init ? '>' : ' class="unread">') +
				'<td>' + (sms[i].incoming ? '&#10525;' : '&#10526;') + '</td>' +
				'<td>' + sms[i].address + '</td>' +
				'<td>' + sms[i].body + '</td>' +
				'<td><nobr>' + formatDate(timestamp) + '</nobr></td></tr>');
			lastid = sms[i].id > lastid ? sms[i].id : lastid; 
		}
		if(!init && sms.length > 0) {
			document.title = 'sms (' + sms.length + ')';
		}
	};
	
	$('body').click(function() {
		document.title = 'sms';
		$('.unread').removeClass('unread');
	});
	
	window.setInterval(function() {
		$.getJSON('msg/list?lastid=' + lastid, addSms); 
	}, 2000);
	
	$.getJSON('msg/list?lastid=0', addSms);
});
