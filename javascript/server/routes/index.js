
/*
 * GET home page.
 */

exports.index = function(req, res){
  var demos = ['basic', 'sensor', 'network', 'dialog'];
  res.render('index', { title: 'Express' , demos: demos});
};
