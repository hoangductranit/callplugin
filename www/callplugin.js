var exec = require('cordova/exec');
var Plugin_ID='callplugin';
var CallNumberPlugin={};

CallNumberPlugin.callNumber= function (successCallback, errorCallback, number){
    exec(successCallback, errorCallback, Plugin_ID, "callNumber", [number]);
};
module.exports=CallNumberPlugin;