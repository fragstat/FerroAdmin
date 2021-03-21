function ImagetoPrint(source) {
    str = '';
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32/api/position/${id}`,
        dataType: 'json',
        async: false,
        success: function (data, textStatus) {
            console.log(data.mark);
            mark = data.mark.replaceAll('–', '-');
            gost = '';
            $.ajax({
                type: 'GET',
                url: `http://5.200.47.32/api/gost?mark=${mark}`,
                async: false,
                success: function (data, textStatus) {
                    console.log(data, `http://5.200.47.32/api/gost?mark=${mark}`);
                    gost = data;
                },
                error: function (data, textStatus) {
                    console.log(data);
                }
            })
            str = "<html><title>Штрих-код</title><head><scri" +
                "pt>function step1(){\n" +
                "setTimeout('step2()', 10);}\n" +
                "function step2(){window.print();window.close()}\n" +
                "</script></head>" + "<body onload='step1()'>\n" +
                "<img width='280' height='100' style='margin: auto;' src='" + source + `' />
            <p style='font-size: 20px; margin: 0'>Производитель: ${data.manufacturer}</p>
            <p style='font-size: 20px; margin: 0'>Марка: ${data.mark}</p>
            <p style='font-size: 20px; margin: 0'>${gost}</p>
            <p style='font-size: 20px; margin: 0'>Диаметр: ${data.diameter}</p>
            <p style='font-size: 20px; margin: 0'>Вид поставки: ${data.packing}</p>
            <p style='font-size: 20px; margin: 0'>Партия: ${data.part}</p>
            <p style='font-size: 20px; margin: 0'>Плавка: ${data.plav}</p>
            <p style='font-size: 20px; margin: 0'>Масса: ${data.mass}</p> 
            
            
</body></html>`;
            console.log(str);
        }
    })
    console.log(str);
    return str;
}

function PrintImage(id) {
    source = `http://5.200.47.32/api/code/${id}`;
    var Pagelink = "about:blank";
    var pwa = window.open(Pagelink, "_new");
    pwa.document.open();
    console.log(ImagetoPrint(source));
    pwa.document.write(ImagetoPrint(source));
    pwa.document.close();
}

$(document).ready(function () {
    console.log(window.location.href.split('?')[1].split('=')[1]);
    id = window.location.href.split('?')[1].split('=')[1];
    $.ajax(
        {
            type: 'GET',
            url: `http://5.200.47.32/api/position/${id}`,
            dataType: 'json',
            success: function (data, textStatus) {
                if (data.status === 'Departured')
                    $('#shipment').hide();
                if (data.status === 'Departured')
                    status = 'Отгружен';
                else if (data.status === 'In_stock')
                    status = 'На складе';
                else if (data.status === 'Arriving')
                    status = 'Прибывает';
                if (data.createdFrom <= 0) {
                    table = `
                    <table class="table">
                    <tbody>
                        <tr>
                        <th>Марка</th>
                        <td>${data.mark}</td>
                        </tr>

                        <tr>
                        <th scope="row">Диаметр</th>
                        <td>${data.diameter}</td>
                        </tr>

                        <tr>
                        <th scope="row">Упаковка</th>
                        <td>${data.packing}</td>
                        </tr>

                        <tr>
                        <th scope="row">Партия</th>
                        <td>${data.part}</td>
                        </tr>

                        <tr>
                        <th scope="row">Плавка</th>
                        <td>${data.plav}</td>
                        </tr>

                        <tr>
                        <th scope="row">Вес</th>
                        <td>${data.mass}</td>
                        </tr>

                        <tr>
                        <th scope="row">Статус</th>
                        <td>${status}</td>
                        </tr>

                        <tr>
                        <th scope="row">Производитель</th>
                        <td>${data.manufacturer}</td>
                        </tr>`;
                    if (data.comment == null || data.comment == "") {
                        console.log(data);
                        $('.info').html(table + `</tbody></table>`);
                    } else {
                        console.log(data);
                        table += `
                        <tr>
                        <th scope="row">Комментарий</th>
                        <td>${data.comment}</td>
                        </tr>`;
                        $('.info').html(table + `</tbody></table>`);
                    }
                } else {
                    table = `
                    <table class="table">
                    <tbody>
                        <tr>
                        <th>Марка</th>
                        <td>${data.mark}</td>
                        </tr>

                        <tr>
                        <th scope="row">Диаметр</th>
                        <td>${data.diameter}</td>
                        </tr>

                        <tr>
                        <th scope="row">Упаковка</th>
                        <td>${data.packing}</td>
                        </tr>

                        <tr>
                        <th scope="row">Партия</th>
                        <td>${data.part}</td>
                        </tr>

                        <tr>
                        <th scope="row">Плавка</th>
                        <td>${data.plav}</td>
                        </tr>

                        <tr>
                        <th scope="row">Вес</th>
                        <td>${data.mass}</td>
                        </tr>

                        <tr>
                        <th scope="row">Статус</th>
                        <td>${status}</td>
                        </tr>

                        <tr>
                        <th scope="row">Производитель</th>
                        <td>${data.manufacturer}</td>
                        </tr>`;
                    if (data.comment == null || data.comment == "") {
                        console.log(data);
                        $('.info').html(table + `</tbody></table>
                            <p>Был в составе <a href="product.html?id=${data.createdFrom}">этого продукта</a></p>`);
                    } else {
                        console.log(data);
                        table += `
                        <tr>
                        <th scope="row">Комментарий</th>
                        <td>${data.comment}</td>
                        </tr>`;
                        $('.info').html(table + `</tbody></table>
                        <p>Был в составе <a href="product.html?id=${data.createdFrom}">этого продукта</a></p>`);
                    }
                }
            }
        }
    )
    $.ajax(
        {
            type: "GET",
            url: `http://ferro-trade.ru/api/history/allById/${id}`,
            dataType: 'json',
            success: function (data, textStatus) {
                console.log(data);
                table = `
            <table class="table">
            <tbody>
                <tr>
                <th>#</th>
                <th>Тип</th>
                <th>Дата</th>
                <th>Место</th>
                </tr>`;
                for (i = 0; i < data.length; i++) {
                    type = data[i].type
                    place = data[i].place
                    if (data[i].type == 'Adding')
                        type = 'Добавление'
                    else if (data[i].type == 'Departure')
                        type = 'Отгрузка'
                    if (data[i].place == 'Solnechnogorsk')
                        place = 'Солнечногорск'
                    table += `
                    <tr>
                    <td>${i + 1}</td>
                    <td>${type}</td>
                    <td>${data[i].date}</td>
                    <td>${place}</td>
                    </tr>`;
                }
                table += '</tbody></table>';
                $(".history").html(table);
            },
            error: function (data, textStatus) {
                console.log(data);
            }
        }
    )
})

$('#controlBtns').delegate('#print', 'click', function () {
    id = window.location.href.split('?')[1].split('=')[1];
    PrintImage(id);
})

$('#controlBtns').delegate('#shipment', 'click', function () {
    $('.shipm-form').css('display', 'inline');
})

$('.shipm-form').delegate('.close-block', 'click', function () {
    $('.shipm-form').fadeOut();
})

$('.shipm-form').delegate('#ship', 'click', function () {
    id = window.location.href.split('?')[1].split('=')[1];
    input = document.forms.shipmentForm.mass.value.replaceAll(",", ".");
    contrAgent = document.forms.shipmentForm.contrAgent.value;
    account = parseInt(document.forms.shipmentForm.account.value, 10);
    console.log(input);
    mass = parseFloat(input);
    console.log(`http://5.200.47.32/api/position/departure?id=${id}&weight=${mass}&contrAgent=${contrAgent}&account=${account}`);
    if (isNaN(mass)) {
        console.log('error');
        $('.error').fadeIn();
        setTimeout(function () {
            $('.error').fadeOut();
        }, 2000);
    } else {
        $.ajax({
            type: 'POST',
            url: `http://5.200.47.32/api/position/departure?id=${id}&weight=${mass}&contrAgent=${contrAgent}&account=${account}`,
            dataType: 'json',
            success: function (data, textStatus) {
                /*console.log(data);
                parent = 0;
                $.ajax({
                    type: 'GET',
                    url: `http://5.200.47.32/api/position/${id}`,
                    dataType: 'json',
                    success: function(data, textStatus)
                    {
                        parent = data.createdFrom;
                    }
                })
                if (parent == -1){
                    $('.print').html(`<a href="#" onclick="PrintImage(${data}); return false;">PRINT</a>`);
                    $('.print').css('display', 'block');
                }
                else{
                    $('.print').html(`<a href="#" onclick="PrintImage(${data}); return false;">PRINT</a>
                    <p>Был в составе <a href="product.html?id=${parent}">этого продукта</a></p>`);
                    $('.print').css('display', 'block');
                }*/
                window.location.href = `product?id=${data}`;
            }
        })
    }
})