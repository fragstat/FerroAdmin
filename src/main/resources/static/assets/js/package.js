function PackageToPrint(id) {
    str = '';
    gost = '1';
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32/api/package?id=${id}`,
        dataType: 'json',
        async: false,
        success: function (data, textStatus) {
            mark = data.mark.replaceAll('–', '-');
            $.ajax({
                type: 'GET',
                url: `http://5.200.47.32/api/gost?mark=${mark}`,
                async: false,
                success: function (data, textStatus) {
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
                "</scri" + "pt></head><body onload='step1()'>\n" +
                "<img width='280' height='100' style='margin: auto;' src='" +
                `http://5.200.47.32/api/code/${id}` + `' />
            <p style='font-size: 20px; margin: 0'>Производитель: ${data.manufacturer}</p>
            <p style='font-size: 20px; margin: 0'>Марка: ${data.mark}</p>
            <p style='font-size: 20px; margin: 0'>${gost}</p>
            <p style='font-size: 20px; margin: 0'>Диаметр: ${data.diameter}</p>
            <p style='font-size: 20px; margin: 0'>Вид поставки: ${data.packing}</p>
            <p style='font-size: 20px; margin: 0'>Партия: ${data.part}</p>
            <p style='font-size: 20px; margin: 0'>Плавка: ${data.plav}</p>
            <p style='font-size: 20px; margin: 0'>Масса: ${data.mass.toFixed(2)}</p>
</body></html>`;
        },
        error: function (data, textStatus) {
            console.log(data);
        }
    })
    return str;
}

function PrintPackage(id) {
    prints = '';
    for (i = 0; i < arguments.length; i++) {
        prints += PackageToPrint(arguments[i]);
    }
    var Pagelink = "";
    var pwa = window.open(Pagelink, "_blank");
    pwa.document.open();
    pwa.document.write(prints);
    pwa.document.close();
}

function CodetoPrint(id) {
    str = '';
    gost = '1';
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32/api/position/${id}`,
        dataType: 'json',
        async: false,
        success: function (data, textStatus) {
            mark = data.mark.replaceAll('–', '-');
            $.ajax({
                type: 'GET',
                url: `http://5.200.47.32/api/gost?mark=${mark}`,
                async: false,
                success: function (data, textStatus) {
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
                "</scri" + "pt></head><body onload='step1()'>\n" +
                "<img width='280' height='100' style='margin: auto;' src='" +
                `http://5.200.47.32/api/code/${id}` + `' />
            <p style='font-size: 20px; margin: 0'>Производитель: ${data.manufacturer}</p>
            <p style='font-size: 20px; margin: 0'>Марка: ${data.mark}</p>
            <p style='font-size: 20px; margin: 0'>${gost}</p>
            <p style='font-size: 20px; margin: 0'>Диаметр: ${data.diameter}</p>
            <p style='font-size: 20px; margin: 0'>Вид поставки: ${data.packing}</p>
            <p style='font-size: 20px; margin: 0'>Партия: ${data.part}</p>
            <p style='font-size: 20px; margin: 0'>Плавка: ${data.plav}</p>
            <p style='font-size: 20px; margin: 0'>Масса: ${data.mass.toFixed(2)}</p>
</body></html>`;
        },
        error: function (data, textStatus) {
            console.log(data);
        }
    })
    return str;
}

function PrintCode(ids) {
    prints = '';
    for (i = 0; i < ids.length; i++) {
        prints += CodetoPrint(ids[i]);
    }
    var Pagelink = "";
    var pwa = window.open(Pagelink, "_blank");
    pwa.document.open();
    pwa.document.write(prints);
    pwa.document.close();
}

/*function card(id, mark, diameter, packing, comment, part, plav, mass, status)
{
    return `<a href="product?id=${id}" class="card product mx-1 my-1" style="width:30%" id="${id}">
        <div class="card-body p-3"> 
            <p class="card-text">Марка: ${mark}</p>
            <p class="card-text">Диаметр: ${diameter}</p>
            <p class="card-text">Упаковка: ${packing}</p>
            <p class="card-text">Партия: ${part}</p>
            <p class="card-text">Плавка: ${plav}</p>
            <p class="card-text">Вес: ${mass.toFixed(2)}</p>
            <p class="card-text">Статус: ${status}</p>
            <p class="card-text">Комментарий: ${comment}</p>
        </div>
    </a>`;
};

function card_without_comm(id, mark, diameter, packing, part, plav, mass, status)
{
    return `<a href="product?id=${id}" class="card product mx-1 my-1" style="width:30%" id="${id}">
        <div class="card-body p-3"> 
            <p class="card-text">Марка: ${mark}</p>
            <p class="card-text">Диаметр: ${diameter}</p>
            <p class="card-text">Упаковка: ${packing}</p>
            <p class="card-text">Партия: ${part}</p>
            <p class="card-text">Плавка: ${plav}</p>
            <p class="card-text">Вес: ${mass.toFixed(2)}</p>
            <p class="card-text">Статус: ${status}</p>
        </div>
    </a>`;
};*/

$(document).ready(function(){
    console.log(window.location.href.split('?')[1].split('=')[1]);
    id = window.location.href.split('?')[1].split('=')[1];
    $.ajax(
        {
            type: 'GET',
            url: `http://5.200.47.32:80/api/package?id=${id}`,
            dataType: 'json',
            success: function(data, textStatus)
            {
                if (data.status == 'Departured')
                    status = 'Отгружен';
                else if (data.status == 'In_stock')
                    status = 'На складе';
                table = `<div class='col-12'>
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
                    <td>${data.mass.toFixed(2)}</td>
                    </tr>

                    <tr>
                    <th scope="row">Статус</th>
                    <td>${status}</td>
                    </tr

                    <tr>
                    <th scope="row">Производитель</th>
                    <td>${data.manufacturer}</td>
                    </tr>`;
                if (data.comment == null || data.comment == ""){
                    console.log(data);
                    table += `</tbody></table></div><div class="col-12 row m-0">`;
                }
                else{
                    console.log(data);
                    table += `
                    <tr>
                    <th scope="row">Комментарий</th>
                    <td>${data.comment}</td>
                    </tr></tbody></table></div><div class="col-12 row m-0">`;
                }
                data.positionsList.forEach(function(product){
                    if (product.status == 'Departured')
                        status = 'Отгружено';
                    else if (product.status == 'In_stock')
                        status = 'На складе';
                    if (product.comment == '' || product.comment == null)
                        table += card_without_comm(product.id, product.mark, product.diameter, product.packing, product.part, product.plav, product.mass, status);
                    else
                        table += card(product.id, product.mark, product.diameter, product.packing, comm, product.part, product.plav, product.mass, status);
                })
                console.log(table + '</div>');
                $('.info').html(table + `</div>`);
            }
        }
    )
})

$('#controlBtns').delegate('#print', 'click', function(){
    id = window.location.href.split('?')[1].split('=')[1];
    PrintPackage(id);
})

$('#controlBtns').delegate('#printAll', 'click', function(){
    id = window.location.href.split('?')[1].split('=')[1];
    $.ajax(
        {
            type: 'GET',
            url: `http://5.200.47.32:80/api/package?id=${id}`,
            dataType: 'json',
            success: function(data, textStatus)
            {
                console.log(data.positionsList);
                ids = [];
                data.positionsList.forEach(function(position){
                    ids.push(position.id);
                })
                console.log(ids);
                PrintCode(ids);
            }
        }
    )
})

$('#controlBtns').delegate('#shipment', 'click', function(){
    $('.shipm-form').css('display', 'inline');
})

$('.shipm-form').delegate('.close-block', 'click', function(){
   $('.shipm-form').fadeOut();   
})