app.controller('CreateMenuController',
    function ($scope, $http, Admin, $state) {
        $scope.nav = false;
        $scope.answer = {
            publish: false,
            description: '',
            name: ''
        };

		var params = {
			access_token: $.cookie("access_token")
		};

		Admin.all_locations(params).success(function (data) {
			$scope.locations = data.data;
		});

        $scope.create_menu = function () {

            Admin.create_menucatalogue($scope.answer).success(function (data) {
                console.log("MENU CREATED" + JSON.stringify($scope.answer));
                $state.go('osn1.menus');
                $scope.answer = null;
                Custombox.close();

                $state.reload();
            });
        };

        $scope.reset = function () {
            $scope.answer = null;
        };
    });




