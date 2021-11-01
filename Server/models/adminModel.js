const {getClient} = require('../Utils/DBConfig');

const adminAuth = async (email, password) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select admin_id, admin_email, admin_name from admins where admin_email = '${email}' and admin_password = '${password}'`);
		await client.end();
		res(result)		
	})
}

module.exports = {adminAuth}
