app.controller('AnswerControllerMenu',
    function ($scope, $stateParams, $http, Admin, $state) {
        $scope.answer = {
            "description": "",
            "id": 0,
            "name": "",
            "price": '',
            "specialities": true,
            "topSeller": true,
            "url": ""
        };

        $scope.update = function () {
            $state.reload();
            $('#CreateMenuModal').modal('hide');
        };

        Admin.all_category().success(function (data) {
            $scope.category = data.data;
            $scope.id = $stateParams.id;

            if ($scope.id) {
                Admin.id_category($scope.id).success(function (data) {
                    $scope.category_detail = data;
                    $scope.create_menu = function () {

                        Admin.create_menu($scope.answer, $scope.id).success(function (data) {
                            $scope.answer = null;
                            $('#CreateMenuModal').modal('hide');
							$('.modal-backdrop').remove();
							$('body').removeClass('modal-open');
                            $state.reload();
                        });
                    };
                });
            }
        });
    });




