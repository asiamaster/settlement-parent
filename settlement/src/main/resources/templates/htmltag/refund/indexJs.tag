<script>
    $(function() {
        $('#btn-certain-refund').click(refundCertainClickHandler);
    });

    /** 退款页面确定按钮点击事件处理器 */
    function refundCertainClickHandler() {
        let mchId = $('[name="mchRadio"]:checked').val();
        let customerId = $('#settle-customer-id').val();
        let customerName = $('#settle-customer-name').val();
        let rows = $('#table_' + mchId).find('[name="chk"]:checked');
        if (null == rows || rows.length === 0) {
            showWarning('请至少选中一条数据');
            return;
        }
        let idList = [];
        for (let row of rows) {
            idList.push($(row).val());
        }
        let url = "/settleOrder/forwardRefund.html?mchId="+mchId+"&customerId="+customerId+"&customerName="+customerName+"&ids="+idList.join(",");
        bs4pop.dialog({
            id:'dialog-refund',
            content:url,
            title:'退款',
            isIframe:true,
            width:'70%',
            height:500,
            btns:[
                {label: '取消',className: 'btn-secondary'},
                {label: '确定',className: 'btn-primary',onClick:dialogCertainClickHandler}
            ]
        });
    }

    /** 退款弹框确定按钮点击事件处理器 */
    function dialogCertainClickHandler(e, $iframe) {
        bui.loading.show('数据验证中，请稍候。。。');
        let win = $iframe[0].contentWindow;
        if (!win.validateRefundForm()) {
            bui.loading.hide();
            return false;
        }
        bui.loading.hide();
        bui.loading.show('努力提交中，请稍候。。。');
        $.ajax({
            type:"POST",
            url:"/settleOrder/refund.action",
            dataType:"json",
            data:$iframe.contents().find('#form-refund').serialize(),
            success:function(result) {
                bui.loading.hide();
                if (result.code === '200') {
                    settleResultHandler(result.data);
                } else {
                    showError(result.message);
                }
            },
            error:function(){
                bui.loading.hide();
                showError("系统异常,请稍后重试");
            }
        });
    }

    let url = '';
    /** 加载客户退款单据处理器 */
    function loadCustomerOrdersHandler(cusId) {
        $('#settle-order-list').removeClass("d-none");
        url = "/settleOrder/listRefundOrders.action?customerId=" + cusId;
        $('#table-settle-order-list').load(url);
    }

    /** 刷新表格处理器 */
    function refreshTableHandler() {
        if (url === '') {
            return;
        }
        $('#table-settle-order-list').load(url);
    }
</script>
