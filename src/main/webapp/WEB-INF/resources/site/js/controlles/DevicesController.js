app.controller('DevicesController', function ($scope, $http, $state, Admin) {

    $scope.nav = false;

    $scope.filters = {
		'Assigned to location': 'BY_LOCATION',
        'Assigned to menu': 'ASSIGNED_TO_MENU',
        'Not assigned': 'NOT_ASSIGNED',
        'All': 'ALL'
    };

    $scope.params = {
        access_token: $.cookie("access_token"),
        filter: $scope.filters['All']
    };

    $scope.selectFilter = function () {
		loadDevices();
	};

    function loadDevices() {
		Admin.all_devices($scope.params).success(function (data) {
			$scope.devices = data.data;

			$scope.maxSize = 1;
		});
	}

	loadDevices();
});