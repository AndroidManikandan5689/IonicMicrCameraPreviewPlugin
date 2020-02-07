var exec = require('cordova/exec');

exports.openCamera = function (arg0, success, error) {
    exec(success, error, 'MicrCameraPreview', 'openCamera', [arg0]);
};
