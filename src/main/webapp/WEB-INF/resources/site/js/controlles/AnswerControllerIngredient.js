app.controller('AnswerControllerIngredient',
    function ($scope, $http, Admin) {
        $scope.nav = $.cookie("access_token") == undefined || $.cookie("access_token") == 'undefined';
        Admin.id_menu($scope.id).success(function (data) {
            $scope.menu_detail = data;

        });

        $scope.create = function () {
            Admin.create($scope.answer).success(function (data) {
                    $scope.answer = null;
                }
            );

        };
    })




