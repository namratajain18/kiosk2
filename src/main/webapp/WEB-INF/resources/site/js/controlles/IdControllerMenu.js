app.controller('IdControllerMenu', function ($scope, $stateParams, $http, Admin) {

	$scope.isUploadingActive = false;

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

        $scope.id = $stateParams.id;

        $.cookie("id1", $scope.id);

		$scope.toggleShowDetails = function (id) {
          $scope.showDetails = !$scope.showDetails;
          $scope.showDetailsId = id;
		};


		if ($scope.id) {
            Admin.id_category($scope.id).success(function (data) {
                $scope.category_detail = data;

                $('[data-plugin="custommodal"]').on('click', function (e) {
                    e.preventDefault();
                });

                if ($scope.category_detail.data.menuItems[0] == undefined) {
                    $scope.error = "There is no items to display!"
                }

                $scope.update_menu = function (answerUP) {
                    Admin.update_menu(answerUP).success(function (Admin) {
                        console.log('update');
                    });
                };

                $scope.update_menu_photo = function (answerUP) {
					$scope.isUploadingActive = true;
                    Admin.upload_image_menu(answerUP.id).success(function (res) {
                        window.currentUploadedFile = undefined;

                        answerUP.url = res.data.url;
                        Custombox.close();
						$scope.isUploadingActive = false;
                    });
                }
            });
        }

        $scope.delete_menu = function (answerUP) {
            var confirmed = confirm('Are you sure you want to delete "' + answerUP.name +'"?');

            if (confirmed) {
                return Admin.delete_menu(answerUP).success(function (data) {
                    Admin.id_category($scope.id).success(function (data) {
                        $scope.category_detail = data;
                    });

                });
            }
        };
});