app.controller('IdControllerIngredient', function ($scope, $stateParams, $http, Admin, $state) {
    $scope.id = $stateParams.id;

    if ($scope.id) {
        Admin.id_menu($scope.id).success(function (data) {
            $scope.menu_detail = data;
            if (!$scope.menu_detail) {
                $scope.error = "There is no ingredients to display!";
            }
        });
    }

    $scope.create_ingredient = function () {
        $scope.id = $stateParams.id;

        Admin.create_ingredient($scope.answer, $scope.id).success(function (data) {
            $scope.answer = null;

            Admin.id_menu($scope.id).success(function (data) {
                $scope.menu_detail = data;
                $scope.error = "";
            });
        });

    };

    $scope.update_ingredient = function (answerUP) {
        Admin.update_ingredient(answerUP).success(function (Admin) {
        });
    };

    $scope.delete_ingredient = function (answerUP) {
        return Admin.delete_ingredient(answerUP).success(function (data) {

            Admin.id_menu($scope.id).success(function (data) {
                $scope.menu_detail = data;
            });
        });
    };
});