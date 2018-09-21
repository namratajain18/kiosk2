var app = angular.module("myApp", ["ngResource", "uiSlider", "ui.bootstrap", "ui.router", "LocalStorageModule"]);


app.config(function ($stateProvider, $urlRouterProvider) {


    $urlRouterProvider.otherwise('/login');

    $stateProvider
        .state('osn', {

            url: '/',
            views: {
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },

                'footer': {templateUrl: 'site/views/footer.html'},
                'user': {template: ''},
                'login': {templateUrl: 'site/views/newlogin.html'}

            }

        })

        .state('osn2', {

            url: '/',
            views: {
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },

                'footer': {templateUrl: 'site/views/footer.html'},
                'user': {template: ''}


            }

        })

        .state('osn1', {

            url: '/',
            views: {
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },
                'content': {
                    templateUrl: 'site/views/updata_category.html',
                },

                'footer': {templateUrl: 'site/views/footer.html'},

            }

        })


        .state('osn1.updata', {

            url: 'user_updata',
            views: {
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },
                'user@': {
                    templateUrl: 'site/views/updata_user.html',
                    controller: 'User'
                },
                'content@': {

                },
            }

        })

        .state('osn1.menus', {

            url: 'menus',
            views: {
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },
                'user@': {
                    templateUrl: 'site/views/menus.html',
                    controller: 'Menus'
                },
                'content@': {

                },
            }
        })

        .state('osn1.locations', {

            url: 'locations',
            views: {
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },
                'user@': {
                    templateUrl: 'site/views/locations.html',
                    controller: 'LocationsController'
                },
                'content@': {

                },
            }
        })
        .state('osn1.devices', {

            url: 'devices',
            views: {
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },
                'user@': {
                    templateUrl: 'site/views/devices.html',
                    controller: ''
                },
                'content@': {

                },
            }
        })

        .state('osn1.categoryID', {
            url: 'category/:id',
            views: {
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },
                'menu@': {
                    templateUrl: 'site/views/look_menu_id.html',
                    controller: 'IdControllerMenu'
                }
                ,
                'content': {
                    templateUrl: 'site/views/updata_category.html',
                    controller: 'Category'
                }

            }

        })


        .state('osn1.add_ingredient', {

            url: 'add_ingredient',
            views: {
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },
                'content@': {
                    templateUrl: 'site/views/add_ingredient.html',
                    controller: 'AnswerControllerIngredient'
                }
            }

        })
        .state('osn1.updata_category', {

            url: 'updata_category',
            views: {
                // 'content@':{
                //     templateUrl: 'views/updata_category.html',
                //     controller: 'Category'
                // },
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },
                'menu@': {
                    templateUrl: 'site/views/look_menu_id.html',
                },
            }

        })
        .state('osn1.add_user', {

            url: 'add_user',
            views: {
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },
                'content@': {
                    templateUrl: 'site/views/add_user.html',
                    controller: 'AnswerController'
                }
            }

        })
        .state('osn1.add_category', {

            url: 'updata_category',
            views: {
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },
                'menu@': {
                    templateUrl: 'site/views/add_category.html',
                    controller: 'AnswerControllerCategory'
                }

                // ,
                // 'content@':{
                //     templateUrl: 'views/updata_category.html',
                //     controller: 'Category'
                // }
            }

        })

        .state('modal', {
            abstract: false,
            parent: 'osn1.updata_category',
            views: {
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },
                // 'content@':{
                //     templateUrl: 'views/updata_category.html',
                //     controller: 'Category'
                // },
                'menu@': {
                    templateUrl: 'site/views/look_menu_id.html'
                    // controller: 'IdControllerMenu'
                }
            },


            onEnter: ['$modal', '$state', function ($modal, $state) {
                console.log('Open modal');
                $modal.open({
                    templateUrl: 'site/views/modal_ingredient.html'
                    // controller: 'IdControllerIngredient'


                })
                    .result.finally(function () {

                    $state.go('osn1.categoryID', {'id': $.cookie("id1")});
                });
            }]
        })
        .state('osn1.updata1', {
            url: '/menu/:id',
            parent: 'modal',
            views: {
                'header': {
                    templateUrl: 'site/views/header.html',
                    controller: 'LoginController'
                },
                'modal@': {
                    templateUrl: 'site/views/modal_ingredient.html'
                }
            }
        })
        
   
         .state('osn2.order', {
            url: 'order/{id}',

            views: {
                'content@': {
                    templateUrl: 'site/views/order-confirm.html',
                    controller: 'orderController'
                }
            }
        
        })

        .state('osn2.login', {

            url: 'login',

            views: {
                'login@': {
                    templateUrl: 'site/views/newlogin.html',
                    controller: 'LoginController'
                },
                'header@': {
                    template: '',
                    controller: 'LoginController'
                }
                , 'user@': {template: ''}
            }

        });


});

