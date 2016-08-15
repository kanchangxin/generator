<#assign
pascalName=DomainObjectName
lowerName=DomainObjectName?lower_case
uncapName=DomainObjectName?uncap_first

>

//接口对象,一般在service.js
.factory('Apis', function ($http, $q, ConnectSev) {
    var ApisObj = {};
    var _apiurl = {
        casetype: {
            add${pascalName}: "/admin/${lowerName}/add",
            del${pascalName}: "/admin/${lowerName}/del",
            upd${pascalName}: "/admin/${lowerName}/upd",
            get${pascalName}List: '/admin/${lowerName}/list'
        }
    };
}




//路由,一般在app.js
.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider



        .state('app.${lowerName}', {
            url: '/admin/${lowerName}',
            templateUrl: 'html/admin/wiki/${lowerName}.html',
            controller: '${pascalName}Ctrl'
        })



    $urlRouterProvider.otherwise('/app/productcertificate');
})
