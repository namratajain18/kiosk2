app.factory('Admin', ['$http', function ($http) {

    /*var API_URL = 'http://207.154.221.244:8080/api/';
    var BASE_URL = 'http://207.154.221.244:8080/api/admin';
    var BASE_URL_category = 'http://207.154.221.244:8080/api/admin/category';
    var BASE_URL_Menu = 'http://207.154.221.244:8080/api/admin/menu-item/';
    var BASE_URL_ingredient = 'http://207.154.221.244:8080/api/admin/ingredient/';*/

    var API_URL = 'http://174.138.58.149:8080/api/';
    var BASE_URL = 'http://174.138.58.149:8080/api/admin';
    var BASE_URL_category = 'http://174.138.58.149:8080/api/admin/category';
    var BASE_URL_Menu = 'http://174.138.58.149:8080/api/admin/menu-item/';
    var BASE_URL_ingredient = 'http://174.138.58.149:8080/api/admin/ingredient/';


    return {

        upload_image: function (catId) {
            var params = {
                access_token: $.cookie("access_token")

            };

            var formData = new FormData();
            formData.append('file', window.currentUploadedFile);

            var req = {
                method: 'POST',
                data: formData,
                url: BASE_URL + '/image/upload-category/' + catId,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": undefined
                },
                params: params
            };

            return $http(req);
        },
        /*
         *
         * /image-controller/imageUploadByMenuItemUsingPOST
         *POST /api/admin/image/upload-menu-item/{itemId}
         * */
        upload_image_menu: function (catId) {
            var params = {
                access_token: $.cookie("access_token")

            };

            var formData = new FormData();
            formData.append('file', window.currentUploadedFile);

            var req = {
                method: 'POST',
                data: formData,
                url: BASE_URL + '/image/upload-menu-item/' + catId,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": undefined
                },
                params: params
            };

            return $http(req);
        },


        all_user: function (params) {

            var req = {
                method: 'GET',
                url: BASE_URL + '/all',
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },
                params: params
            };

            return $http(req);
        },

		all_devices: function (params) {

			var req = {
				method: 'GET',
				url: API_URL + '/device/filter',
				headers: {
				//	"Authorization": "Basic " + btoa("test:test"),
					"Content-type": "application/json"
				},
                params: params
			};

			return $http(req);
		},

		device_to_location: function (params, id) {

			var req = {
				method: 'POST',
				url: API_URL + '/device/assign-location/' + id,
				headers: {
				//	"Authorization": "Basic " + btoa("test:test"),
					"Content-type": "application/json"
				},
                params: params
			};

			return $http(req);
		},

		device_to_menu: function (params, id) {

			var req = {
				method: 'POST',
				url: API_URL + '/device/assign-menu/' + id,
				headers: {
				//	"Authorization": "Basic " + btoa("test:test"),
					"Content-type": "application/json"
				},
				params: params
			};

			return $http(req);
		},


		create_device: function (data) {
			var params = {
				access_token: $.cookie("access_token")
			};

			var req = {
				method: 'POST',
				url: API_URL + '/device/init/',
				headers: {
				//	"Authorization": "Basic " + btoa("test:test"),
					"Content-type": "application/json"
				},
				params: params,
                data: data
			};

			return $http(req);
		},

        all_locations: function (params) {

            var req = {
                method: 'GET',
                url: BASE_URL + '/location/all',
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },
                params: params
            };

            return $http(req);
        },

        get_menu_devices: function (params, id) {

            var req = {
                method: 'GET',
                url: BASE_URL + '/location/menu/' + id,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },
                params: params
            };

            return $http(req);
        },

        get_location: function (id) {
			var params = {
				access_token: $.cookie("access_token")
			};

            var req = {
                method: 'GET',
                url: BASE_URL + '/location/' + id,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },
				params: params
            };

            return $http(req);
        },

        add_location: function (data) {
			var params = {
				access_token: $.cookie("access_token")
			};

            var req = {
                method: 'POST',
                url: BASE_URL + '/location/new',
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },
                params: params,
                data: data
            };

            return $http(req);
        },


        create_user: function (answerNew) {

            var params = {
                access_token: $.cookie("access_token")

            };

            var req = {
                method: 'POST',
                url: BASE_URL + '/new',
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },
                params: params,
                data: answerNew
            }

            return $http(req);

        },

        update_user: function (answerUP) {
            var params = {

                access_token: $.cookie("access_token")


            };

            var req = {
                method: 'PUT',
                url: BASE_URL + '/' + answerUP.id,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },

                params: params,
                data: answerUP
            }


            return $http(req);

        },
        delete_user: function (answerUP) {

            var params = {

                access_token: $.cookie("access_token")


            };

            var req = {
                method: 'DELETE',
                url: BASE_URL + '/' + answerUP,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },

                params: params
            }

            return $http(req);
        },

        pullOrder: function (params) {
        	var id = params.id;
        	//http://localhost:8080/kiosk/api/admin/order/0TPRRlNGZVZ?access_token=9bee9994-8bef-4861-b3b9-b8c25d67e9db
            var req = {
                method: 'GET',
                url: BASE_URL + '/order/'+id+'/details',///order/{orderId}/details
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },
                params: params
            };

            return $http(req);
        },
        
        all_menu: function (params) {

            var req = {
                method: 'GET',
                url: BASE_URL + '/menu/all',
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },
                params: params
            };

            return $http(req);
        },

        menu_publish: function (menuId) {
            var params = {
                access_token: $.cookie("access_token")
            };

            var req = {
                method: 'GET',
                url: BASE_URL + '/menu/publish/' + menuId,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },
                params: params
            };

            return $http(req);
        },

        create_menucatalogue: function (answerNew) {
            var params = {
                access_token: $.cookie("access_token")
            };

            var req = {
                method: 'POST',
                url: BASE_URL + '/menu/' + answerNew.locationId,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },
                params: params,
                data: answerNew
            };

            return $http(req);

        },

        all_category: function (data) {
            var params = {
                access_token: $.cookie("access_token"),
                perPage: 0,
                page: 0
            };

            var req = {
                method: 'GET',
                url: BASE_URL_category + '/all',
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },

                params: params
            };

            return $http(req);
        },


        all_category_by_id: function (menuId) {
            var params = {
                access_token: $.cookie("access_token"),
                perPage: 0,
                page: 0
            };

            menuId = menuId || 0;

            var req = {
                method: 'GET',
                url: BASE_URL_category + '/by-menu/' + menuId,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },

                params: params
            };

            return $http(req);
        },

        id_category: function (id) {
            var params = {
                access_token: $.cookie("access_token")
            };

            var req = {
                method: 'GET',
                url: BASE_URL_category + '/' + id,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },

                params: params
            }

            return $http(req);

        },
        create_category: function (answerNew) {
            var params = {
                access_token: $.cookie("access_token")
            };

            var menuId = $.cookie("currentMenuEditing");

            var req = {
                method: 'POST',
                url: BASE_URL_category + '/' + menuId,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },
                params: params,
                data: answerNew
            };

            return $http(req);

        },
        update_category: function (answerUP) {


            var params = {
                catName: answerUP.name,
                access_token: $.cookie("access_token")


            };

            var req = {
                method: 'PUT',
                url: BASE_URL_category,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },

                params: params,
                data: answerUP
            }

            return $http(req);


        },
        delete_category: function (answerUP) {

            var params = {

                access_token: $.cookie("access_token")


            };

            var req = {
                method: 'DELETE',
                url: BASE_URL_category + '/' + answerUP.id,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },

                params: params,

            }


            return $http(req);


        },
        id_menu: function (id) {
            var params = {
                access_token: $.cookie("access_token"),
                perPage: 0,
                page: 0
            };

            var req = {
                method: 'GET',
                url: BASE_URL_Menu + id,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },

                params: params
            }

            return $http(req);

        },
        delete_menu: function (answerUP) {

            var params = {

                access_token: $.cookie("access_token")


            };

            var req = {
                method: 'DELETE',
                url: BASE_URL_Menu + answerUP.id,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },

                params: params,
            }

            return $http(req);
        },
        update_menu: function (answerUP) {


            var params = {

                access_token: $.cookie("access_token")


            };

            var req = {
                method: 'PUT',
                url: BASE_URL_Menu + answerUP.id,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },

                params: params,
                data: answerUP
            }

            return $http(req);


        },


        create_menu: function (answerNew, id) {
            var params = {
                access_token: $.cookie("access_token")

            };

            var req = {
                method: 'POST',
                url: BASE_URL_Menu + id,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },
                params: params,
                data: answerNew
            };

            return $http(req);

        },
        create_ingredient: function (answerNew, id_menu) {
            var params = {
                access_token: $.cookie("access_token")

            };

            var req = {
                method: 'POST',
                url: BASE_URL_ingredient + id_menu,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },
                params: params,
                data: answerNew
            }

            return $http(req);

        },
        update_ingredient: function (answerUP) {


            var params = {
                catName: answerUP.id,
                access_token: $.cookie("access_token")


            };

            var req = {
                method: 'PUT',
                url: BASE_URL_ingredient + answerUP.id,
                headers: {
                    //"Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },

                params: params,
                data: answerUP
            }

            return $http(req);


        },
        delete_ingredient: function (answerUP) {

            var params = {

                access_token: $.cookie("access_token")


            };

            var req = {
                method: 'DELETE',
                url: BASE_URL_ingredient + answerUP.id,
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },

                params: params,

            }


            return $http(req);


        },
        logout: function (data) {

            var params = {

                access_token: $.cookie("access_token")


            };

            var req = {
                method: 'GET',
                //url: "http://207.154.221.244:8080/api/admin/logout",
                url: "http://174.138.58.149:8080/api/admin/logout",
                headers: {
                  //  "Authorization": "Basic " + btoa("test:test"),
                    "Content-type": "application/json"
                },

                params: params,

            }


            return $http(req);


        }
    };
}]);
