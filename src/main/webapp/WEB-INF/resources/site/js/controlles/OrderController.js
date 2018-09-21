app.controller('orderController', function ($scope, $http, $state, $stateParams, Admin) {

    $scope.nav = false;

    var params = {
        //access_token: $.cookie("access_token"),
        //perPage: 7,
        //page: 0,
        id:$stateParams.id
    };
    
    $scope.restaurant = 'Buckhorn Grill';
    $scope.logo = 'https://buckhorngrill.com/wp-content/uploads/2016/01/Logo-14-14-14.png';
    // http://localhost:8080/kiosk/api/admin/order/0TPRRlNGZVZ?access_token=bc760b7e-68bd-4a34-9116-267cf743b7d3
    $scope.order;
    console.log("param passed in url:"+$stateParams.id);
    
    //Admin.pullOrder(params)
    Admin.pullOrder(params).success(function (data) {
        console.log("data:"+data);
        $scope.order = data.data;
        $scope.restaurant = 'Test';
    });


});