exports.basic = function(req, res){
    res.render('basic', { title: 'Basic' });
};

exports.sensor = function(req, res){
    res.render('sensor', { title: 'Sensor' });
};

exports.network = function(req, res){
    res.render('network', { title: 'Network' });
};
