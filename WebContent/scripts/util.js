function formatMoney(n, c, d, t) {
	var c = isNaN(c = Math.abs(c)) ? 2 : c;
	var d = d == undefined ? "." : d;
	var t = t == undefined ? "," : t;
	var s = n < 0 ? "-" : "";
	var i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "";
	var j = (j = i.length) > 3 ? j % 3 : 0;
	return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
};

function formatDate(data) {
	//var dateString = data.getDate()  + "-" + (data.getMonth()+1) + "-" + data.getFullYear();
	var dateString = ("0" + data.getDate()).slice(-2) + "/" + ("0" + (data.getMonth() + 1)).slice(-2) + "/" + data.getFullYear();
	return dateString;
}

function getMeses() {
	var meses = [{ key: 1, value: 'Janeiro' }, { key: 2, value: 'Fevereiro' }, { key: 3, value: 'Março' }, { key: 4, value: 'Abril' },
		{ key: 5, value: 'Maio' }, { key: 6, value: 'Junho' }, { key: 7, value: 'julho' }, { key: 8, value: 'Agosto' },
		{ key: 9, value: 'Setembro' }, { key: 10, value: 'Outubro' }, { key: 11, value: 'Novembro' }, { key: 12, value: 'Dezembro' }
	];
	return meses;
}

function fixMes(mes) {
	var response = mes;
	if (mes > 12) {
		response = mes - 12;
	} else if (mes < 1) {
		response = 12 - mes;
	}
	return response;
}

function labelMes(mesNum) {
	var mesFix = fixMes(eval(mesNum));
	var mesPos = eval(mesFix) - 1;
	return (getMeses()[mesPos]).value;
}
