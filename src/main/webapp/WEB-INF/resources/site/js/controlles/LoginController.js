app.controller('LoginController',
    function ($scope, $resource, $http, $httpParamSerializer, $state, Admin, $location, $rootScope) {
        $scope.loginBGLoc = $location.protocol() + '://' + $location.host() + ':' + $location.port() + '/assets/images/login.jpg';
        $scope.logout = function () {
            $.cookie("access_token", undefined);
            Admin.logout();

            $state.go("osn2.login", {}, {reload: true});
            $scope.nav = true;
            $state.reload();

        };
        $rootScope.allowleftbar = false;

        $scope.nav = $.cookie("access_token") == undefined || $.cookie("access_token") == 'undefined';
        $rootScope.allowleftbar = $.cookie("access_token") == undefined || $.cookie("access_token") == 'undefined';

        $scope.data = {
            grant_type: "password"
        };

        $scope.encoded = btoa("test:test");

        $scope.login = function (data) {

            var req = {
                method: 'POST',
                //url: "http://207.154.221.244:8080/oauth/token",
                url: "http://174.138.58.149:8080/oauth/token",
                headers: {
                    //"Authorization": "Basic " + $scope.encoded,
                    "Content-type": "application/x-www-form-urlencoded; charset=utf-8"
                },
                data: $httpParamSerializer(data)

            };

            $http(req).then(
                function (data) {
                    $.cookie("access_token", data.data.access_token);
                    $.cookie("refresh_token", data.data.refresh_token);

                    $scope.nav = false;
                    $rootScope.allowleftbar = true;

                    $state.go('osn1.updata_category', {}, {reload: true});

                },
                function (error) {
                    $scope.error = "Incorrect login or password!"
                }
            );
        };
    });