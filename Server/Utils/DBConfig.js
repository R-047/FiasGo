const {Client} = require("pg")
const options = {
	user: "vamzmlbsbqatom",
	password: "b2d471c3678c8417e77366797887f1a07a4b05ee170332143d66174c64f45ca1",
	host: "ec2-54-145-102-149.compute-1.amazonaws.com",
	port: 5432,
	database: "d8g462ei571gi1",
	ssl:  { rejectUnauthorized: false }
}

const getClient = () => {
	return new Client(options);
}


module.exports = {getClient};

