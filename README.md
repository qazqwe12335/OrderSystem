# OrderSystem

透過firebase達成會員驗證，根據會員權限分辨客人或店家，進入相對應頁面
客人 - 點餐頁面
店家 - 訂單頁面

<img src="app screenshot/ordersystemanim.jpg">
<img src="app screenshot/ordersystemlogin.jpg">
<img src="app screenshot/orderlist.jpg">

點餐頁面 至firebase databse抓取菜單內容，根據分類顯示於頁面中。
選擇餐點後，進入調整甜度冰塊及杯數，相同甜度冰塊的商品 購物車會以累加方式顯示，而非兩列
至購物車頁面點擊確認，購物車商品傳入firebse database中的訂單區並標記訂單編號。

<img src="app screenshot/orderchoose.jpg">
<img src="app screenshot/ordercart.jpg">

訂單頁面會根據取餐時間做排序

<img src="app screenshot/order.jpg">
