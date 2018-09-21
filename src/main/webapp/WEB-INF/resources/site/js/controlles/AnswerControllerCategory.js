app.controller('AnswerControllerCategory',
    function ($scope, $http, Admin, $state) {
        $scope.close = function () {
            Custombox.close();
        };

        $scope.menueItems = [];

        $scope.add = function (Items) {
            $scope.menueItems.push($scope.Items);
        };

        $scope.create_category = function () {
            $scope.nav = false;
            Admin.create_category($scope.answer).success(function (data) {
                $scope.answer.menueItems = $scope.menueItems;
                $scope.answer = null;
                // $state.reload();
                $scope.nav = false;
                $state.go("osn1.updata_category", {}, {reload: true});
            });
            $('#CreateCatModal').modal('hide');


        };

        $scope.reset = function () {
            $scope.answer = null;
        };


    });




