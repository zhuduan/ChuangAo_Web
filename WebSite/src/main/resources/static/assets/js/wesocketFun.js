	
	
	
		//--- change the show nums
		function statisAccountsNum(){			
			var receiveAccountsNum = $("#receiveTable").find("tr").size() - 1;
			var sendAccountsNum = $("#sendTable").find("tr").size() - 1;
			var unknowAccountsNum = $("#unknowTable").find("tr").size() - 1;
			if(receiveAccountsNum<0){
				receiveAccountsNum = 0;
			}
			if(sendAccountsNum<0){
				sendAccountsNum = 0;
			}
			if(unknowAccountsNum<0){
				unknowAccountsNum = 0;
			}
			var totalAccountsNum = receiveAccountsNum + sendAccountsNum + unknowAccountsNum;
			$("#totalAccountsNum").html(totalAccountsNum);
			$("#receiveAccountsNum").html(receiveAccountsNum);
			$("#sendAccountsNum").html(sendAccountsNum);
			$("#unknowAccountsNum").html(unknowAccountsNum);
		}
		
		//--- websocket info
		var stompClient = null; 
		var userID = 0;
		function setCurrentUserID(){
			userID = $("#userID").val();		
		}		
		function connect() {        
			var socket = new SockJS('/websocket');        
			stompClient = Stomp.over(socket);        
			stompClient.connect({}, function (frame) {            
				 sendInfo(userID); 	//tell the server add a new observer
				 listenInfo();				
			});    
		}    
		// 断开socket连接
		function disconnect() {        
			if (stompClient != null) {       
				var handleUrl = "/ws/app/commonService/onlineMonitor/" + userID +"/delete";
				stompClient.send(handleUrl, {}, userID); 
				stompClient.disconnect();        
			}        
			setConnected(false);        
			console.log("Disconnected");    
		}    
		// 向‘/ws/app/commonService/onlineMonitor/{userID}’服务端发送消息
		function sendInfo(value) { 
			var handleUrl = "/ws/app/commonService/onlineMonitor/" + userID;
			stompClient.send(handleUrl, {}, value);    
		}    
		// 监听‘/ws/topic/commonService/onlineMonitor/{userID}’的消息
		function listenInfo(){
			var listenUrl = "/ws/topic/commonService/onlineMonitor/" + userID;
			stompClient.subscribe(listenUrl, function (data) {  
				var adjustData = data.body.toString().substr(1,data.body.toString().length-2).replace(/\\/g,"");
				setChangedAccount(adjustData);
			});    
		}
		function getShowInfo(status){
			switch(status){
				case 0:
					return "<span class=\"am-text-warning\">状态未知</span>";
				case 1:
					return "<span class=\"am-text-secondary\">鉴权中</span>";
				case 2:
					return "<span class=\"am-text-success\">正常连接</span>";
				case 3:
					return "<span class=\"am-text-success\">正在更新</span>";
				case 4:
					return "<span lass=\"am-text-danger\">断开</span>";
				case 5:
					return "<span class=\"am-text-secondary\">反馈</span>";
				case 6:
					return "<span class=\"am-text-secondary\">无订单</span>";	
				case 7:
					return "<span class=\"am-text-danger\">丢失心跳</span>";
				default:
					return "<span lass=\"am-text-danger\">未知服务器错误</span>";
			}
		}
		function getNewTr(accountID,accountStatus,accountType){
			var result = "<tr id=\"" + accountID + "\">" 
							+ "<td class=\"am-text-center\">*</td>"
							+ "<td>" + accountID + "</td>"
							+ "<td>" + getShowInfo(accountStatus) + "</td>"
							+ "<td style=\"display:none\">" + accountType + "</td>"
							+ "</tr>";
			return result;
		}
		function setChangedAccount(ouputJson){
			var jsonData = $.parseJSON(ouputJson);
			var accountID = jsonData.accountID;
			var accountStatus = jsonData.accountStatus;
			var accountType = jsonData.accountType;
			var tagetTR = "#"+accountID;
			var currentType = $(tagetTR).find("td").last().html();
			if(currentType==0 && accountType>0){
				//--- remove from the unknow and add to new type	
				if(accountType==1){
					//---to sender
					$(tagetTR).remove();
					$("#sendTable").find("tbody").append(getNewTr(accountID,accountStatus,accountType));					
				} else if(accountType==2){
					//---to receiver
					$(tagetTR).remove();
					$("#receiveTable").find("tbody").append(getNewTr(accountID,accountStatus,accountType));					
				} else {
					alert("type error: "+accountType);
				}
			} else if(accountType==currentType){
				//--- just change the status
				$(tagetTR).find("span").parent().html(getShowInfo(accountStatus));
			} else{
				//--- add a new one to the other table, and do not change the original one
				if($(tagetTR).size()<2){
					if(accountType==1){
						//---to sender
						$("#sendTable").find("tbody").append(getNewTr(accountID,accountStatus,accountType));
					} else if(accountType==2){
						//---to receiver
						$("#receiveTable").find("tbody").append(getNewTr(accountID,accountStatus,accountType));
					} else {
						alert("type error: "+accountType);
					}
				} else{
					$(tagetTR).last().find("span").parent().html(getShowInfo(accountStatus));
				}
			}
			statisAccountsNum();
		}