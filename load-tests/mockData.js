const { v4: uuidv4 } = require('uuid');

module.exports = {
    generateUniqueUser: function (userContext, events, done) {
      const randomId = uuidv4();
      userContext.vars.username = `user${randomId}`;
      userContext.vars.email = `user${randomId}@gmail.com`;
      userContext.vars.password = "Password123";
      return done();
    }
  };