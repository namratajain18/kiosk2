app.controller('CreateLocationController',
    function ($scope, $http, Admin, $state) {
        $scope.nav = false;
        $scope.answer = {
            "name": ''
        };

        $scope.create_location = function () {
            Admin.add_location($scope.answer).success(function () {
                $state.go('osn1.locations');
                $scope.answer = null;
                Custombox.close();

                $state.reload();
            });
        };

        $scope.reset = function () {
            $scope.answer = null;
        };
    });




