app.controller('Category', function ($scope, $http, Admin, $location, $anchorScroll, $state, $timeout, $rootScope) {
    $scope.locations = null;
    $scope.currentLocation = null;
    $scope.getImageUrl = function (item) {
        var expression = /[-a-zA-Z0-9@:%_\+.~#?&//=]{2,256}\.[a-z]{2,4}\b(\/[-a-zA-Z0-9@:%_\+.~#?&//=]*)?/gi;
        var regex = new RegExp(expression);

        function isValidUrl(url) {
            return url.match(regex);
        }

        if (!item.imgUrl && !item.url) return 'site/image/food1.jpg';

        //var url = 'http://207.154.221.244/' + (item.imgUrl || item.url);
        var url = 'http://174.138.58.149/' + (item.imgUrl || item.url);

        url = encodeURI(url);

        if (url.length && isValidUrl(url)) {
            return url;
        } else {
            return 'site/image/food1.jpg';
        }
    };

    var params = {
        access_token: $.cookie("access_token")
    };
    $scope.selectLoc = function(location) {
        $scope.currentLocation = location;
        $.cookie("currentLocationInCategories", JSON.stringify($scope.currentLocation));
        $scope.hideShit = true;
        $('#ThatCrappyDiv').hide();
    };
    if (!angular.isUndefined($.cookie("currentLocationInCategories"))) {
        console.log($.cookie("currentLocationInCategories"));
        $scope.currentLocation = JSON.parse($.cookie("currentLocationInCategories"));
    }

    $scope.selectMenu = function(menu) {
        $.cookie("currentMenuEditing", menu.id);
        $.cookie("currentMenuEditingName", menu.description);
        $state.go("osn1.updata_category", {}, {reload: true});
        $rootScope.hideShit = false;
    };
    
    Admin.all_locations(params).success(function (data) {
		$scope.locations = data.data;
		$scope.locations.forEach(function (location) {
			updateLocationInfo(location);
		});
        console.log($scope.locations);
	});

	function updateLocationInfo(loc) {
		Admin.get_location(loc.id).success(function (data) {
			loc.menus = data.data.menus;
			loc.devices = data.data.devices;

			var devices = data.data.devices;

			loc.menus.forEach(function (menu) {
				menu.locationDevices = angular.copy(devices);
			})
		});
	}

    $scope.activeMenuId = $.cookie("currentMenuEditing");
    $scope.activeMenuDesc = $.cookie("currentMenuEditingName");
	$scope.isUploadingActive = false;

    Admin.all_menu(params).success(function (data) {
        $scope.menus = data.data;
        $scope.activeMenuId = $.cookie("currentMenuEditing");
        $scope.activeMenuDesc = $.cookie("currentMenuEditingName");
    }).then(
        Admin.all_category_by_id($scope.activeMenuId).success(function (data) {
            $scope.category = data.data;

            $scope.update_image = function (answerUP, $index) {
				$scope.isUploadingActive = true;

                Admin.upload_image(answerUP.id).success(function (res) {
                    window.currentUploadedFile = undefined;

                    answerUP.imgUrl = res.data.imgUrl;
                    $('#custom-modal-photo' + $index).modal('hide');
					$scope.isUploadingActive = false;
                })
            };

            $scope.update_category = function (answerUP, $index) {
                Admin.update_category(answerUP).success(function (Admin) {

                    console.log('update');
                    $('#custom-modal-edit' + $index).modal('hide');
                });
            };

            $scope.delete_category = function (answerUP, $index) {
                return Admin.delete_category(answerUP).success(function (data) {
                    $state.go("osn1.updata_category", {}, {reload: true});
                    $scope.nav = false;

                    $('#custom-modal-deletecat' + $index).modal('hide');
                });


            };
        })
    );
});
