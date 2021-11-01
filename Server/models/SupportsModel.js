
const {getClient} = require('../utils/DBConfig');

const supportVictim = async(user_id, desc, post_id, list_item_id, quantity, timestamp) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		try {
		await client.connect();
		await client.query('begin');
		console.log("started trans")
		console.log("inserting supports")
		await client.query(`insert into supports(support_user_id,support_desc,support_post_id,list_item_id,quantity,date) values(${user_id},'${desc}', ${post_id}, ${list_item_id},${quantity},'${timestamp}')`);
		console.log("updating the items recieved count")
		await client.query(`update needy_items set received = (select received from needy_items where list_item_id = ${list_item_id}) + ${quantity} where list_item_id = ${list_item_id}`)
		console.log("updating supports count")
		await client.query(` update victims_post set v_count_supports = (select v_count_supports from victims_post where v_post_id = ${post_id}) + 1 where v_post_id = ${post_id}`)
		await client.query('commit');
		console.log("commited trans")
		res(true)
		} catch (error) {
			console.log("rolling back"+error)
			await client.query("rollback");
			res(false)
		} finally {
			await client.end();
		}			
	})
}


const updateSupportStatus = async(status, support_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`update supports set support_status = '${status}' where support_id = ${support_id}`);
		await client.end();
		res(result)		
	})
}



const getVictimsItems = async (v_post_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select needy_items.*, users.user_name from needy_items join users using(user_id) where needy_items.v_post_id = ${v_post_id}`);
		await client.end();
		res(result.rows)		
	})

}


const getSupports = async () => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select supports.*, users.user_name, users.user_email, users.ph_num from supports join users on supports.support_user_id = users.user_id`);
		await client.end();
		res(result)		
	})
}

const getPostSupports = async(v_post_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select supports.*, users.user_name, users.user_email, users.ph_num from supports join users on supports.support_user_id = users.user_id where support_post_id = ${v_post_id}`);
		await client.end();
		res(result.rows)		
	})
}


const getUserSupports = async(user_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select supports.*, needy_items.user_id as victim_user_id, needy_items.list_item_name from supports join needy_items using(list_item_id) where support_user_id = ${user_id}`);
		await client.end();
		res(result)		
	})
}





module.exports = {supportVictim,getVictimsItems,updateSupportStatus,getSupports,getPostSupports,getUserSupports}