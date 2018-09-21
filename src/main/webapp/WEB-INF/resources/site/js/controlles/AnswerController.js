app.controller('AnswerController',
    function ($scope, $http, Admin, $state) {
        $scope.nav = false;
        $scope.bans = ["BANNED", "ENABLED", "FAILURES_COUNT", "LOGIN_FROM_MULTI_IPS", "BANNED_BY_ADMIN"];

        $scope.role = ["ROLE_ADMIN", "ROLE_EMPLOYEE", "CUSTOMER", "TRIAL_USER", "FULL_USER", "EXPIRED_USER"];

        $scope.create_user = function () {

            Admin.create_user($scope.answer).success(function (data) {
                $state.go('osn.updata');
                $scope.answer = null;
                Custombox.close();

                $state.reload();

            });

        };

        $scope.reset = function () {
            $scope.answer = null;
        };
    });




