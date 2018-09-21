app.controller('LocationsController', function ($scope, $http, $state, Admin) {
	$scope.nav = false;

	var params = {
		access_token: $.cookie("access_token")
	};

	var devicesParams = {
		access_token: $.cookie("access_token"),
		filter: 'NOT_ASSIGNED'
	};

	Admin.all_locations(params).success(function (data) {
		$scope.locations = data.data;
		$scope.locations.forEach(function (location) {
			updateLocationInfo(location);
		});

		loadDevices();
	});

	function updateLocationInfo(loc) {
		Admin.get_location(loc.id).success(function (data) {
			loc.menus = data.data.menus;
			loc.devices = data.data.devices;

			var devices = data.data.devices;

			loc.menus.forEach(function (menu) {
				menu.locationDevices = angular.copy(devices);

				loadDevicesForMenu(menu);
			})
		});
	}

	function loadDevices () {
		Admin.all_devices(devicesParams).success(function (data) {
			$scope.devices = data.data;

			$scope.devices.forEach(function (device) {
				device.checked = false;
			});

			$scope.locations.forEach(function (loc) {
				loc.locationDevices = angular.copy($scope.devices);
			});
		});
	}

	function isDeviceInList(device, deviceList) {
		return deviceList.some(function (dev) {
			return dev.id === device.id;
		});
	}

	function loadDevicesForMenu (menu) {
		Admin.get_menu_devices(params, menu.id).success(function (res) {
			menu.devices = res.data;

			menu.locationDevices.forEach(function (device) {
				device.checked = isDeviceInList(device, menu.devices);
			});
		});
	}

	$scope.update_location = function (loc) {
		Admin.add_location({'name': loc.name, 'id': loc.id}).success(function () {
			alert('Location added.');
		});
	};

	function mapDevicesIds(devices) {
		return devices
			.filter(function (item) {
				return item.checked
			})
			.map(function (item) {
				return item.id;
			})
			.join(',');
	}

	$scope.assignDevicesToLoc = function (loc) {
		var devicesIds = mapDevicesIds(loc.locationDevices);

		var reqParams = {
			access_token: $.cookie("access_token"),
			deviceIds: devicesIds
		};

		if (devicesIds.length) {
			Admin.device_to_location(reqParams, loc.id).success(function () {
				updateLocationInfo(loc);
				loadDevices();
			})
		}
	};

	$scope.assignDevicesToMenu = function (loc, menu) {
		var devicesIds = mapDevicesIds(menu.locationDevices);

		var reqParams = {
			access_token: $.cookie("access_token"),
			deviceIds: devicesIds
		};

		if (devicesIds.length) {
			Admin.device_to_menu(reqParams, menu.id).success(function () {
				updateLocationInfo(loc);
				loadDevices();
			})
		}
	}
});