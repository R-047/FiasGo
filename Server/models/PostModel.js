const {getClient} = require('../utils/DBConfig');


const getVictimsFeed = async () => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select  victims_post.*, user_name from victims_post join users using(user_id)`);
		let final_result = [];
		for(const element of result.rows){
			const post_id = element.v_post_id;
			const images_result = await client.query(`select victims_post_image_id, victims_post_image_name from post_images_victims group by victims_post_image_id having victims_post_id = ${post_id};`);
			element.images = images_result.rows;
			final_result.push(element);
		}
		await client.end();
		res(final_result)		
	})
}



const getServicesFeeds = async (user_lat, user_long) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select  service_post.*, user_name, st_distance(st_setsrid(st_makepoint(${user_lat}, ${user_long}), 4326), service_post.services_location) from service_post join users using(user_id) where st_distance(st_setsrid(st_makepoint(${user_lat}, ${user_long}), 4326), service_post.services_location) < 30000`);
		let final_result = [];
		for(const element of result.rows){
			const post_id = element.s_post_id;
			const images_result = await client.query(`select postimageid,imagename from post_images_services group by postimageid having postid = ${post_id};`);
			element.images = images_result.rows;
			final_result.push(element);
		}
		await client.end();
		res(final_result)		
	})
}





const uploadServicesPost = async(user_id, contact_info, s_post_desc, post_images_names, post_lat, post_long, location_name, date) => {
	return new Promise(async(res, rej) => {
		const client = getClient();
		try {
		await client.connect();
		await client.query('begin');
		console.log("started trans")
		console.log("inserting post")
		await client.query(`insert into service_post(s_reports_count,s_upvotes,s_share_count,contact_info,s_post_desc,user_id,comments_count,services_location,services_lat,services_long,service_location_name,date) values(${0}, ${0}, ${0}, '${contact_info}', '${s_post_desc}', ${user_id},${0},'point(${post_lat} ${post_long})', ${post_lat}, ${post_long},'${location_name}','${date}')`);
		console.log("getting the post_id")
		const result = await client.query(`select s_post_id from service_post where user_id=${user_id} order by s_post_id desc limit 1`);
		console.log("inserting post images")
		const post_id = result.rows[0].s_post_id;
		for(const element of post_images_names){
			await client.query(`insert into post_images_services (postid,imagename) values(${post_id},'${element}')`);
		}
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



const uploadVictimsPost = async (user_id, v_post_desc, post_images_names, victims_location, needys_items_arr, date) => {
	return new Promise(async(res, rej) => {
		const client = getClient();
		try {
		await client.connect();
		await client.query('begin');
		console.log("started trans")
		console.log("inserting post")
		await client.query(`insert into victims_post(v_count_shares,v_reports_count,v_post_desc,v_upvotes,user_id, v_count_supports, victims_location_name, date) values(${0}, ${0}, '${v_post_desc}', ${0}, ${user_id}, ${0}, '${victims_location}', '${date}')`);
		console.log("getting the post_id")
		const result = await client.query(`select v_post_id from victims_post where user_id=${user_id} order by v_post_id desc limit 1`);
		console.log("inserting post images")
		const post_id = result.rows[0].v_post_id;
		for(const element of post_images_names){
			await client.query(`insert into post_images_victims(victims_post_id, victims_post_image_name) values(${post_id},'${element}')`);
		}
		for(const element of needys_items_arr){
			await client.query(` insert into needy_items(v_post_id,user_id,list_item_name,limit_quantity,received,item_desc) values(${post_id},${user_id},'${element.item_name}',${element.limit},${0},'${element.item_desc}')`)
		}
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

// insert into victims_post_upvotes values(1, 55);
// update victims_post set v_upvotes = (select v_upvotes from victims_post where v_post_id = 54) + 1 where v_post_id = 54;
const upvoteVictimsPost = async (user_id, v_post_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		try {
		await client.connect();
		await client.query('begin');
		console.log("started trans")
		console.log("inserting upvotes")
		await client.query(`insert into victims_post_upvotes values(${user_id}, ${v_post_id})`);
		console.log("updating upvotes count")
		await client.query(`update victims_post set v_upvotes = (select v_upvotes from victims_post where v_post_id = ${v_post_id}) + 1 where v_post_id = ${v_post_id}`);
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


const upvoteServicesPost = async (user_id, s_post_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		try {
		await client.connect();
		await client.query('begin');
		console.log("started trans")
		console.log("inserting upvotes")
		await client.query(`insert into service_post_upvotes values(${user_id}, ${s_post_id})`);
		console.log("updating upvotes count")
		await client.query(`update service_post set s_upvotes = (select s_upvotes from service_post where s_post_id = ${s_post_id}) + 1 where s_post_id = ${s_post_id}`);
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

// delete from victims_post_upvotes where user_id = 1 and victims_post_id = 54
const downvoteVictimsPost = async (user_id, v_post_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		try {
		await client.connect();
		await client.query('begin');
		console.log("started trans")
		console.log("deleting upvotes")
		await client.query(`delete from victims_post_upvotes where user_id = ${user_id} and victims_post_id = ${v_post_id}`);
		console.log("updating upvotes count")
		await client.query(`update victims_post set v_upvotes = (select v_upvotes from victims_post where v_post_id = ${v_post_id}) - 1 where v_post_id = ${v_post_id}`);
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


const downvoteServicePost = async (user_id, s_post_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		try {
		await client.connect();
		await client.query('begin');
		console.log("started trans")
		console.log("deleting upvotes")
		await client.query(`delete from service_post_upvotes where user_id = ${user_id} and service_post_id = ${s_post_id}`);
		console.log("updating upvotes count")
		await client.query(`update service_post set s_upvotes = (select s_upvotes from service_post where s_post_id = ${s_post_id}) - 1 where s_post_id = ${s_post_id}`);
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

// insert into comments(comment,user_id,date,time,service_post_id) values('whats up', 1, '2020-10-27', '20:00', 62);

const PostComment = async (user_id, service_post_id, comment, date, time) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		try {
		await client.connect();
		await client.query('begin');
		console.log("started trans")
		console.log("posting comments")
		await client.query(`insert into comments(comment,user_id,date,time,service_post_id) values('${comment}', ${user_id}, '${date}', '${time}', ${service_post_id})`);
		console.log("updating comments count")
		await client.query(`update service_post set comments_count = (select comments_count from service_post where s_post_id = ${service_post_id}) + 1 where s_post_id = ${service_post_id}`);
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

const getPostComment = async (s_post_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select * from comments where service_post_id=${s_post_id}`);
		await client.end();
		res(result.rows)		
	})
}


const deletePostComment = async (comment_id, s_post_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		try {
		await client.connect();
		await client.query('begin');
		console.log("started trans")
		console.log("deleting comment")
		await client.query(`delete from comments where comment_id = ${comment_id}`);
		console.log("updating upvotes count")
		await client.query(`update service_post set comments_count = (select comments_count from service_post where s_post_id = ${s_post_id}) - 1 where s_post_id = ${s_post_id}`);
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




const takeDownVictimsPost = async(v_post_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`delete from victims_post where v_post_id = ${v_post_id}`);
		await client.end();
		res(result)		
	})
}

// 
const takeDownServicesPost = async(s_post_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`delete from service_post where s_post_id = ${s_post_id}`);
		await client.end();
		res(result)		
	})
}

// select * from victims_post where v_post_id = 56;
const getVictimsPost = async (v_post_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select * from victims_post where v_post_id = ${v_post_id}`);
		const images_result = await client.query(`select victims_post_image_id, victims_post_image_name from post_images_victims group by victims_post_image_id having victims_post_id = ${v_post_id}`);
		if(result.rows[0]){
			result.rows[0].images_result = images_result.rows;
			await client.end();
		}
		await client.end();
		res(result)		
	})
}

const getServicePost = async (s_post_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select * from service_post where s_post_id = ${s_post_id}`);
		const images_result = await client.query(`select postimageid,imagename from post_images_services group by postimageid having postid = ${s_post_id}`);
		if(result.rows[0]){
			result.rows[0].images_result = images_result.rows;
			await client.end();
		}
		await client.end();
		res(result)		
	})
}

const getUsersVictimsPost = async (user_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select  victims_post.*, user_name from victims_post join users using(user_id) where user_id = ${user_id}`);
		let final_result = [];
		for(const element of result.rows){
			const post_id = element.v_post_id;
			const images_result = await client.query(`select victims_post_image_id, victims_post_image_name from post_images_victims group by victims_post_image_id having victims_post_id = ${post_id}`);
			element.images = images_result.rows;
			final_result.push(element);
		}
		await client.end();
		res(final_result)		
	})
}

const getUsersServicesPost = async (user_id) => {
	return new Promise(async (res, rej) => {
		const client = getClient();
		await client.connect();
		const result = await client.query(`select  service_post.*, user_name from service_post join users using(user_id) where user_id = ${user_id}`);
		let final_result = [];
		for(const element of result.rows){
			const post_id = element.s_post_id;
			const images_result = await client.query(`select postimageid,imagename from post_images_services group by postimageid having postid = ${post_id};`);
			element.images = images_result.rows;
			final_result.push(element);
		}
		await client.end();
		res(final_result)		
	})
}




module.exports = {getVictimsFeed, getServicesFeeds, uploadServicesPost, uploadVictimsPost, upvoteVictimsPost, upvoteServicesPost, downvoteVictimsPost, downvoteServicePost, PostComment, getPostComment, deletePostComment, takeDownVictimsPost,  takeDownServicesPost, getVictimsPost, getServicePost, getUsersVictimsPost, getUsersServicesPost};
