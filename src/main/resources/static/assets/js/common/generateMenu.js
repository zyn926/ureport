$(function(){
    var menuArry = $.parseJSON($("#menusList").val());
    /*var menuArry = [{name:"角色管理",pid:1,id:3,url:"role/page"},{name:"系统管理",pid:0,id:1},{name:"用户管理",pid:1,id:2,url:"auUser/page"}];*/
    //菜单列表html
    var menus = '';
    generateMenu(0,menuArry);
    $("#side-menu").append(menus).metisMenu();

    function generateMenu(id,menuArry){
        var childArry = findChildren(id, menuArry);
        if (childArry.length > 0) {
            if(id != 0){
                menus += "<ul class='nav nav-second-level'>";
            }
            for (var i in childArry) {
                var curl = (childArry[i].url != null && childArry[i].url != undefined && childArry[i].url != "") ? childArry[i].url : "#";
                var cArry = findChildren(childArry[i].id,menuArry);
                if(cArry.length > 0){
                    if(curl === "#"){
                        menus += "<li class=''>" +
                            "<a  href='"+curl+"' >" +
                            "<i class='"+childArry[i].icon+"'></i>" +
                            "<span class='nav-label'>"+childArry[i].name+ "</span>" +
                            "<span class='fa arrow'></span>" +
                            "</a>";
                    }else{
                        menus += "<li class='J_menuItem'>" +
                            "<a href='"+curl+"' class='J_menuItem' name='tabMenuItem'>" +
                            "<i class='"+childArry[i].icon+"'></i>" +
                            "<span class='nav-label'>"+childArry[i].name+"</span>" +
                            "<span class='fa arrow'></span>" +
                            "</a>";
                    }
                    generateMenu(childArry[i].id, menuArry);
                    menus += "</li>";
                }else{
                    if(curl === "#"){
                        menus += "<li class=''>" +
                            "<a href='"+curl+"' >" +
                            "<i class='"+childArry[i].icon+"'></i>" +
                            "<span class='nav-label'>"+childArry[i].name+"</span>" +
                            "</a>";
                    }else{
                        menus += "<li class='J_menuItem'>" +
                            "<a href='"+curl+"' class='J_menuItem' name='tabMenuItem'>" +
                            "<i class='"+childArry[i].icon+"'></i>" +
                            "<span class='nav-label'>"+childArry[i].name+"</span>" +
                            "</a>";
                    }
                    generateMenu(childArry[i].id, menuArry);
                    menus += "</li>";
                }

            }
            if(id != 0){
                menus += "</ul>";
            }
        }
    }
    //根据菜单主键id获取下级菜单
    //id：菜单主键id
    //arry：菜单数组信息
    function findChildren(id, arry) {
        var newArry = new Array();
        for (var i in arry) {
            if (arry[i].pid == id){
                newArry.push(arry[i]);
                //delete arry[i];
            }
        }
        return newArry;
    }
});