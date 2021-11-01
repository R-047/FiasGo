function getTime(){
	const date = new Date();
	const today_date = [date.getFullYear(), date.getMonth()+1, date.getDate()].join("-");
	const today_time = [date.getHours(), date.getMinutes(), date.getSeconds()].join(":");
	return [today_date, today_time].join(" ");
}

module.exports = {getTime}