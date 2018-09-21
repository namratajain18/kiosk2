app.controller('CreateDeviceController',
    function ($scope, $http, Admin, $state) {
        $scope.nav = false;

        $scope.deviceTypes = {
        	'KITCHEN': 0,
			'TVSCREEN': 1,
			'TABLET': 2
		};

        $scope.answer = {
            deviceType: '',
            sysId: '',
			uid: ''
        };

		var params = {
			access_token: $.cookie("access_token")
		};

		Admin.all_locations(params).success(function (data) {
			$scope.locations = data.data;
		});

        $scope.create_device = function () {

            Admin.create_device($scope.answer).success(function (data) {
				$state.go('osn1.devices');
                $scope.answer = {
					deviceType: '',
					sysId: '',
					uuid: ''
				};
                Custombox.close();

                $state.reload();
            });
        };

        $scope.reset = function () {
            $scope.answer = null;
        };
    });




