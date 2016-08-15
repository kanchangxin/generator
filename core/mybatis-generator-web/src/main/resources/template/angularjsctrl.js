/*  v1:初始建造，author:自动生成,date: ${createDate?datetime?string.short}
    <#--
    针对angularjs controller
    生成的模板不同项目需要按照自己的格式修改
    -->
*/

starter.controller("${DomainObjectName}Ctrl", function ($rootScope, $scope,Apis) {

    $scope.$watch('$viewContentLoaded', function () {
        init();
    });

    /**
     * 初始化整个函数，页面加载后自动执行
     */
    function init() {
        $scope.item = {};//增删改时用的临时量
        $scope.currentpage = 1;//当前页
        $scope.getList();
    }

    /**
     * 查询方法
     * @param page
     */
    $scope.getList = function (page) {
        var data = {
            name: $scope.name
        };
        if (page == undefined) {
            page = 1;
            $scope.currentpage = page;
        }
        else {
            $scope.currentpage = page;
        }

        Apis.get${DomainObjectName}List(data, page).then(function (res) {
            if (res.rtnCode == 0) {
                $scope.modelEntrys = res.bizData;
                if (res.bizData.length == 0  || res.bizData.list.length==0 )
                    $rootScope.toastInfo("没有符合查询条件的数据")
            } else {
                $rootScope.toastError(res.msg);
            }
        }, function (res) {
            $rootScope.toastError(res.msg);
        });
    }

    /**
     * 输入验证
     */
    $scope.inputVerify=function(){
        <#list DomainObjectPropertys as col>
            <#if col.getJavaProperty()!="id">
                <#if col.isNullable()==false>
        if ($.trim($scope.item.${col.getJavaProperty()}) == "") {
        $rootScope.toastInfo("${col.getRemarks()}不能为空");
            return false;
        }
                </#if>
        if($.trim($scope.item.${col.getJavaProperty()}).length>${col.getLength()}){
            $rootScope.toastInfo("${col.getRemarks()}长度不能超过${col.getLength()}");
            return false;
        }
            </#if>
        </#list>
        return true;
    }
    /**准备新增*/
    $scope.createItem = function () {
        <#list DomainObjectPropertys as col>
            <#if col.getJavaProperty()!="id"><#-- id 作为主键必须排除，否则信则个修改的界面会出问题 -->
        $scope.item.${col.getJavaProperty()} = "";
            </#if>
        </#list>
    }


    $scope.add = function (closeMe) {
        if($scope.inputVerify()==false)return;
        var data = {
            <#list DomainObjectPropertys as col>
            ${col.getJavaProperty()}: $scope.item.${col.getJavaProperty()}<#if col_has_next>,</#if>
            </#list>
        };
        Apis.add${DomainObjectName}(data).then(function (res) {
            $rootScope.toastSuccess("新增成功");
            $scope.getList();
            if (closeMe) {
                $('#myModal').modal('hide');
            } else {
                $scope.createItem();
            }
        }, function (res) {
            $rootScope.toastError(res.msg);
        });
    }


    $scope.del = function (item) {
        if(confirm("确定删除？")==false)return;
        var data = {
            id: item.id
        }
        Apis.del${DomainObjectName}(data).then(function (res) {
            $scope.modelEntrys.list.splice($.inArray(item, $scope.modelEntrys), 1);
        }, function (res) {
            $rootScope.toastError(res.msg);
        });
    }


    /**准备修改*/
    $scope.mdfItem = function (item) {
        $scope.item = item;
    }

    $scope.upd = function () {
        if($scope.inputVerify()==false)return;

        var data = {
            <#list DomainObjectPropertys as col>
            ${col.getJavaProperty()}: $scope.item.${col.getJavaProperty()}<#if col_has_next>,</#if>
            </#list>
         };

        Apis.upd${DomainObjectName}(data).then(function (res) {
            $scope.modelEntrys.list.splice($.inArray($scope.item, $scope.modelEntrys), 1);
            $scope.modelEntrys.list.push($scope.item);
            $('#myModal').modal('hide');
        }, function (res) {
            $rootScope.toastError(res.msg);
        });
    }
});