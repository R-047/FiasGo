const path = require("path")
function getRootDir() {
	return __dirname.slice(0,-5)
}

console.log(getRootDir());
module.exports =  {getRootDir};

