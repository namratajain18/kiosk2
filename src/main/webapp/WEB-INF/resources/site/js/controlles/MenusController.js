app.controller('Menus', function ($scope, $http, $state, Admin) {

    $scope.nav = false;

    var params = {
        access_token: $.cookie("access_token")
    };

    $scope.publishMenu = function (menuId) {
      Admin.menu_publish(menuId).success(function () {
          $state.reload();
      });
    };

    Admin.all_menu(params).success(function (data) {
        $scope.menus = data.data;
        console.log(data);
        $scope.maxSize = 1;
    });


});