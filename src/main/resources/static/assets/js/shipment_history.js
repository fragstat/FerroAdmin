$(document).ready(function(){
    id = window.location.href.split('?')[1].split('=')[1];
    $.ajax(
    {
        type: 'GET',
        url: `http://5.200.47.32:80/api/history/departure/${id}`,
        dataType: 'json',
        success: function(data, textStatus)
        {
            console.log(data);
            $(".contrAgent").html(`Агент: ${data.contrAgent}`);
            $(".account").html(`Счет: ${data.bill}`);
            $(".date").html(`Дата: ${data.date}`);
            products = '';
            data.positions.forEach(function(product){
                status = 'Отгружено';
                if (product.comment == '' || product.comment == null)
                    products += card_without_comm(product.id, product.mark, product.diameter, product.packing, product.part, product.plav, product.mass, status);
                else
                    products += card(product.id, product.mark, product.diameter, product.packing, comm, product.part, product.plav, product.mass, status);
            });
            $(".cards").html(products);
        },
        error: function(data, textStatus)
        {
            console.log(data);
        }
    })
})