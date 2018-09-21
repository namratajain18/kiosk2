app.controller('User', function ($scope, $http, $state, Admin) {

    $scope.nav = false;


    $scope.role = ["ROLE_ADMIN", "ROLE_EMPLOYEE", "CUSTOMER", "TRIAL_USER", "FULL_USER", "EXPIRED_USER"];

    $scope.ban = ["BANNED", "ENABLED", "FAILURES_COUNT", "LOGIN_FROM_MULTI_IPS", "BANNED_BY_ADMIN"];

    var params = {
        access_token: $.cookie("access_token"),
        perPage: 7,
        page: 1
    };

    if (typeof  $.cookie("userCurrentPage") == 'undefined') {
        params.page = 0;

    }
    else {
        params.page = $.cookie("userCurrentPage") - 1;

    }

    Admin.all_user(params).success(function (data) {
        $scope.users = data.data;

        $scope.totalElements = data.pageContainer.totalElements;

        $scope.viewby = 7;
        $scope.totalItems = $scope.totalElements;
        $scope.currentPage = 1;

        $scope.qw = function () {
            $.cookie("userCurrentPage", $scope.currentPage);

            $state.reload();
        };

        if (typeof  $.cookie("userCurrentPage") == 'undefined') {
            $scope.currentPage = 0;
        }
        else {
            $scope.currentPage = $.cookie("userCurrentPage");
        }

        $scope.itemsPerPage = $scope.viewby;
        $scope.maxSize = 1;

        $scope.update_user = function (answerUP) {
            Admin.update_user(answerUP).success(function (Admin) {
                console.log('update');
            });
        };


        $scope.delete_user = function (answerUP) {
            var confirmed = confirm('Are you sure you want to delete this user?');

            if (confirmed) {
                return Admin.delete_user(answerUP).success(function (data) {
                    console.log(answerUP);
                    Admin.all_user(params).success(function (data) {
                        $scope.users = data.data;
                    });
                });
            }
        };
    });


});