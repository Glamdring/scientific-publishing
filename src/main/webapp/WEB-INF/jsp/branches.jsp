<%@page contentType="text/html" pageEncoding="UTF-8"%>

   <link rel="stylesheet" href="${staticRoot}/css/jquery.tree.min.css">
   <script src="${staticRoot}/js/jquery.tree.min.js"></script>
   
   <script type="text/javascript">
       var branchListFullyVisible = true; 
       
       $(document).ready(function() {
    	   // delaying the loading with a second so that it doesn't interfere with the visual page loading
    	   setTimeout(function() {
	           var json = '${scienceBranchesJson}';
	           var branches = $.parseJSON(json);
	           var container = $("#branches");
	           var elements = [];
	           appendBranch(elements, branches);
	           container.append(elements.join(""));
	           container.tree({
	               onCheck: { 
	                   ancestors: 'nothing', 
	                   descendants: 'nothing' 
	               }, 
	               onUncheck: { 
	                   ancestors: 'nothing' 
	               }
	           })
	           
	           
	           $("#branchSearchBox").keyup(function() {
	               delay(function() {
	                   $("#branches li").removeClass("found show collapseChildren");
	                   var text = $("#branchSearchBox").val().toLowerCase();
	
	                   // only start filtering after the 2nd character
	                   if (text.length < 3) {
	                       text = "";
	                   }
	                   // avoid duplicate traversals if the list is already visible
	                   if (!text && branchListFullyVisible) {
	                       return;
	                   }
	                   var ops = 0;
	                   $(".branchName").each(function() {
	                       var branch = $(this);
	                       if (branch.text().toLowerCase().indexOf(text) <= -1) {
	                           branch.parent().hide(); // hide the li
	                       } else {
	                           var currentLi = branch.parent();
	                           currentLi.addClass("found");
	                           if (!currentLi.hasClass("leaf")) {
	                               // collapse all children, so that they are accessible
	                               currentLi.addClass("collapseChildren");
	                               currentLi.find("li").each(function() {
	                                   $(this).addClass("show");
	                               });
	                           }
	                           // (if the input is empty, all nodes will be shown anyway)
	                           if (text) {
	                               // also show all parents of the li to the top
	                               while (currentLi.parent().parent().prop("tagName").toLowerCase() != "div") {
	                                   currentLi = currentLi.parent().parent();
	                                   if (!currentLi.hasClass("found")) {
	                                       currentLi.addClass("found");
	                                       currentLi.removeClass("collapseChildren");
	                                   } else {
	                                       break;
	                                   }
	                               }
	                           }
	                       }
	                   });
	                   
	                   // now expand all visible ones
	                   $("#branches .show").each(function() {
	                       $(this).show();
	                   });
	                   $("#branches .found").each(function() {
	                       var currentLi = $(this);
	                       currentLi.show();
	                       if (currentLi.parent().parent().prop("tagName").toLowerCase() != "div") {
	                           container.tree("expand", currentLi.parent().parent());
	                       }
	                       if (currentLi.hasClass("collapseChildren")) {
	                           container.tree("collapse", currentLi);
	                       }
	                   });
	                   if (!text) {
	                       branchListFullyVisible = true;
	                   } else {
	                       branchListFullyVisible = false;
	                   }
	               }, 700);
	           });
    	   }, 1000);
	   });

       function appendBranch(container, branches) {
           container.push("<ul>");
           for (var i = 0; i < branches.length; i ++) {
               var branch = branches[i]; 
               var cssClass ="collapsed";
               if (branch.children.length == 0) {
                   cssClass = "leaf";
               }
               container.push('<li class="' + cssClass + '" id="scienceBranchLi' + branch.id +'"><input type="checkbox" name="scienceBranch" id="scienceBranch' + branch.id + 
                   '" value="' + branch.id + '" /><label for="scienceBranch' + branch.id + '" class="branchName">' + branch.name + '</label>');

               if (branches.length > 0) {
                   appendBranch(container, branch.children);
               }
               container.push("</li>");
               
           }
           container.push("</ul>");
       }
       
       var delay = (function(){
         var timer = 0;
         return function(callback, ms){
           clearTimeout (timer);
           timer = setTimeout(callback, ms);
         };
       })();
   </script>
</head>
<input type="text" class="ui-input ui-widget form-control" id="branchSearchBox" style="width: 650px;" placeholder="Search..." />
<div id="branches" style="height: 310px; width: 650px; overflow: auto;">
</div>