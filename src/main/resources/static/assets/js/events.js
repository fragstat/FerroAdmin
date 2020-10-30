function search_help(tags) {
    $("#searchValue").autocomplete({
        source: tags
    });
}

function onInput() {
    var input = document.forms.searchForm.searchValue.value;
    if (input != '') {
        ssyl = `http://5.200.47.32/api/search/tag/${input}`;
        console.log(ssyl);
        $.ajax(
            {
                type: 'GET',
                url: ssyl,
                dataType: 'json',
                success: function (data, textStatus) {
                    console.log(data);
                    search_help(data);
                }
            })
    }
}

$('.form').delegate("#add", "click", function () {
    console.log('works');
    mark = document.forms.regForm.mark.value;
    diameter = document.forms.regForm.diameter.value;
    packing = document.forms.regForm.packing.value;
    plav = document.forms.regForm.plav.value;
    part = document.forms.regForm.part.value;
    mass = document.forms.regForm.mass.value;
    comment = document.forms.regForm.comment.value;
    manufacturer = document.forms.regForm.manufacturer.value;
    console.log(mark, diameter, packing, plav, part, mass, comment);
    ssyl = `http://5.200.47.32/api/position/add?manufacturer=${manufacturer}&mark=${mark}&diameter=${diameter}&packing=${packing}&plav=${plav}&part=${part}&mass=${mass}&comment=${comment}`;
    console.log(ssyl);
    $.ajax(
        {
            type: 'POST',
            url: ssyl,
            dataType: "json",
            success: function (data, textStatus) {
                console.log(data);
                window.open(`product?id=${data}`, '_blank');
                //$('.form').css("display", "none");
                document.forms.regForm.mark.value = '';
                document.forms.regForm.diameter.value = '';
                document.forms.regForm.plav.value = '';
                document.forms.regForm.part.value = '';
                document.forms.regForm.mass.value = '';
                document.forms.regForm.comment.value = '';
                document.forms.regForm.manufacturer.value = '';
                //$('.print').html(`<a href="#" onclick="PrintImage('http://5.200.47.32/api/code/${data}'); return false;">PRINT</a>`);
                //$('.print').css("display", "inline");
            },
            error: function (data, textStatus) {
                $('.error').fadeIn();
                setTimeout(function () {
                    $('.error').fadeOut();
                }, 2000);
            }
        });
})

function pack(id, mark, diameter, packing, comment, part, plav, mass, status) {
    return `<a href="package?id=${id}" class="card product mx-1 my-1" style="width:30%" id="${id}">
        <div class="card-body p-3"> 
            <div class="col-12 text-center" style="font-size: 19px;">Поддон</div>
            <p class="card-text">Марка: ${mark}</p>
            <p class="card-text">Диаметр: ${diameter}</p>
            <p class="card-text">Упаковка: ${packing}</p>
            <p class="card-text">Партия: ${part}</p>
            <p class="card-text">Плавка: ${plav}</p>
            <p class="card-text">Вес: ${mass}</p>
            <p class="card-text">Статус: ${status}</p>
            <p class="card-text">Комментарий: ${comment}</p>
        </div>
    </a>`;
}

function pack_without_comm(id, mark, diameter, packing, part, plav, mass, status) {
    return `<a href="package?id=${id}" class="card product mx-1 my-1" style="width:30%" id="${id}">
        <div class="card-body p-3"> 
            <div class="col-12 text-center" style="font-size: 19px;">Поддон</div>
            <p class="card-text">Марка: ${mark}</p>
            <p class="card-text">Диаметр: ${diameter}</p>
            <p class="card-text">Упаковка: ${packing}</p>
            <p class="card-text">Партия: ${part}</p>
            <p class="card-text">Плавка: ${plav}</p>
            <p class="card-text">Вес: ${mass}</p>
            <p class="card-text">Статус: ${status}</p>
        </div>
    </a>`;
}

function card(id, mark, diameter, packing, comment, part, plav, mass, status) {
    return `<a href="product?id=${id}" class="card product mx-1 my-1" style="width:30%" id="${id}">
        <div class="card-body p-3"> 
            <div class="col-12 text-center" style="font-size: 19px;">Позиция</div>
            <p class="card-text">Марка: ${mark}</p>
            <p class="card-text">Диаметр: ${diameter}</p>
            <p class="card-text">Упаковка: ${packing}</p>
            <p class="card-text">Партия: ${part}</p>
            <p class="card-text">Сплав: ${plav}</p>
            <p class="card-text">Вес: ${mass}</p>
            <p class="card-text">Статус: ${status}</p>
            <p class="card-text">Комментарий: ${comment}</p>
        </div>
    </a>`;
}

function card_without_comm(id, mark, diameter, packing, part, plav, mass, status) {
    return `<a href="product?id=${id}" class="card product mx-1 my-1" style="width:30%" id="${id}">
        <div class="card-body p-3"> 
            <div class="col-12 text-center" style="font-size: 19px;">Позиция</div> 
            <p class="card-text">Марка: ${mark}</p>
            <p class="card-text">Диаметр: ${diameter}</p>
            <p class="card-text">Упаковка: ${packing}</p>
            <p class="card-text">Партия: ${part}</p>
            <p class="card-text">Сплав: ${plav}</p>
            <p class="card-text">Вес: ${mass}</p>
            <p class="card-text">Статус: ${status}</p>
        </div>
    </a>`;
}

function card_depart(id, mark, diameter, packing, part, plav, mass, status) {
    return `<div class="card product mx-1 my-1" style="width:30%" id="${id}">
        <div class="card-body"> 
            <p class="card-text">Марка: ${mark}</p>
            <p class="card-text">Диаметр: ${diameter}</p>
            <p class="card-text">Упаковка: ${packing}</p>
            <p class="card-text">Партия: ${part}</p>
            <p class="card-text">Плавка: ${plav}</p>
            <p class="card-text">Вес: ${mass}</p>
            <p class="card-text">Статус: ${status}</p>,
            <label for="mass">Масса отгрузки</label>
            <input class="mass form-control col-10" name="mass" id="mass ${id}"/>
        </div>
    </div>`;
}

function card_to_print(id) {
    str = '';
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32/api/position/${id}`,
        dataType: 'json',
        async: false,
        success: function (data, textStatus) {
            mark = data.mark;
            diameter = data.diameter;
            packing = data.packing;
            part = data.part;
            plav = data.plav;
            mass = data.mass;
            str += `
         <div class="card product mx-1 my-1" style="width:30%" id="${id}">
            <div class="card-body"> 
               <p class="card-text">Марка: ${mark}</p>
               <p class="card-text">Диаметр: ${diameter}</p>
               <p class="card-text">Упаковка: ${packing}</p>
               <p class="card-text">Партия: ${part}</p>
               <p class="card-text">Плавка: ${plav}</p>
               <p class="card-text">Вес: ${mass}</p>
               <button class="btn btn-outline-info btn-block mb-2" id="print" onclick='PrintCode(${id}); return false;'>Печать</button>
            </div>
         </div>`;
        },
        error: function () {
            console.log(data, textStatus);
        }
    })
    return str;
}

function card_for_many_reg(checkMark, checkDiameter, checkPacking, checkPart, checkPlav, checkMass, checkManufacturer, checkComment, mark, diameter, packing, part, plav, mass, manufacturer, comment) {
    card = `<div class="card productInput mx-1 my-1" style="width:30%"><div class="card-body">`;
    if (checkMark)
        card += `<p class="card-text">Марка: ${mark}</p>`;
    else
        card += `<div class="row col-12">
      <label for="mark">Марка</label>
      <input class="form-control" name="mark" id="mark" />
      </div>`;
    if (checkDiameter)
        card += `<p class="card-text">Диаметр: ${diameter}</p>`;
    else
        card += `<div class="row col-12">
      <label for="diameter">Диаметр</label>
      <input class="form-control" name="diameter" id="diameter" />
      </div>`;
    if (checkPacking)
        card += `<p class="card-text">Упаковка: ${packing}</p>`;
    else
        card += `<div class="row col-12">
      <label for="packing">Упаковка</label>
      <input class="form-control" name="packing" id="packing" />
      </div>`;
    if (checkPart)
        card += `<p class="card-text">Партия: ${part}</p>`;
    else
        card += `<div class="row col-12">
      <label for="part">Партия</label>
      <input class="form-control" name="part" id="part" />
      </div>`;
    if (checkPlav)
        card += `<p class="card-text">Плавка: ${plav}</p>`;
    else
        card += `<div class="row col-12">
      <label for="plav">Плавка</label>
      <input class="form-control" name="plav" id="plav" />
      </div>`;
    if (checkMass)
        card += `<p class="card-text">Вес: ${mass}</p>`;
    else
        card += `<div class="row col-12">
      <label for="mass">Вес</label>
      <input class="form-control" name="mass" id="mass" />
      </div>`;
    if (checkManufacturer)
        card += `<p class="card-text">Производитель: ${manufacturer}</p>`;
    else
        card += `<div class="row col-12">
      <label for="manufacturer">Производитель</label>
      <input class="form-control" name="manufacturer" id="manufacturer" />
      </div>`;
    if (checkComment)
        card += `<p class="card-text">Комментарий: ${comment}</p>`;
    else
        card += `<div class="row col-12">
      <label for="comment">Комментарий</label>
      <input class="form-control" name="comment" id="comment" />
      </div>`;
    return card + `</div></div>`;
}

$('.addManyForm').delegate("#addMany", "click", function () {
    console.log('works');
    mark = document.forms.regManyForm.markMany.value;
    diameter = document.forms.regManyForm.diameterMany.value;
    packing = document.forms.regManyForm.packingMany.value;
    plav = document.forms.regManyForm.plavMany.value;
    part = document.forms.regManyForm.partMany.value;
    mass = document.forms.regManyForm.massMany.value;
    comment = document.forms.regManyForm.commentMany.value;
    manufacturer = document.forms.regManyForm.manufacturerMany.value;
    count = document.forms.regManyForm.number.value;
    console.log(mark, diameter, packing, plav, part, mass, comment, manufacturer);
    $('.addManyForm').fadeOut();
    checked = [];
    elems = $("input:checkbox[name=addManyFormCheck]:checked");
    for (i = 0; i < elems.length; i++) {
        checked.push(elems[i].id);
    }
    console.log(checked);
    products = '';
    product = card_for_many_reg(checked.includes('mark'), checked.includes('diameter'), checked.includes('packing'), checked.includes('part'),
        checked.includes('plav'), checked.includes('mass'), checked.includes('manufacturer'), checked.includes('comment'), mark, diameter, packing,
        part, plav, mass, manufacturer, comment);
    for (i = 0; i < count; i++) {
        products += product;
    }
    btn = `<button class="btn btn-outline-success btn-block" id="regMany">Зарегистрировать</button>`;
    $("#cards").html(btn + products);
    $("#cards").unbind('click').delegate('#regMany', 'click', function () {
        $("#regMany").prop("disabled", true);
        elems = $('.productInput');
        objs = [];
        for (i = 0; i < elems.length; i++) {
            if (checked.includes('mark'))
                queryMark = mark;
            else
                queryMark = $(elems[i].lastChild).find('#mark')[0].value;

            if (checked.includes('diameter'))
                queryDiameter = diameter;
            else
                queryDiameter = $(elems[i].lastChild).find('#diameter')[0].value;

            if (checked.includes('packing'))
                queryPacking = packing;
            else
                queryPacking = $(elems[i].lastChild).find('#packing')[0].value;

            if (checked.includes('part'))
                queryPart = part;
            else
                queryPart = $(elems[i].lastChild).find('#part')[0].value;

            if (checked.includes('plav'))
                queryPlav = plav;
            else
                queryPlav = $(elems[i].lastChild).find('#plav')[0].value;

            if (checked.includes('mass'))
                queryMass = mass;
            else
                queryMass = $(elems[i].lastChild).find('#mass')[0].value;

            if (checked.includes('manufacturer'))
                queryManufacturer = manufacturer;
            else
                queryManufacturer = $(elems[i].lastChild).find('#manufacturer')[0].value;

            if (checked.includes('comment'))
                queryComment = comment;
            else
                queryComment = $(elems[i].lastChild).find('#comment')[0].value;
            queryMass = parseFloat(queryMass.replaceAll(',', '.'));
            console.log(queryMark, queryDiameter, queryPacking, queryPart, queryPlav, queryMass, queryManufacturer, queryComment);
            obj = {
                "mark": queryMark,
                "diameter": queryDiameter,
                "packing": queryPacking,
                "comment": queryComment,
                "part": queryPart,
                "plav": queryPlav,
                "manufacturer": queryManufacturer,
                "mass": queryMass
            }
            objs.push(obj);
        }
        console.log(objs);
        $.ajax({
            type: 'POST',
            url: `http://5.200.47.32:80/api/multipleAdd`,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(objs),
            dataType: 'json',
            success: function (data, textStatus) {
                console.log(data);
                products = ``;
                products = `<div class="row col-12"><button class="btn btn-outline-info btn-block mb-2" onclick="PrintCode(${data.id}); return false;">Распечатать все</button></div>
             <div class="row col-12"><button class="btn btn-outline-info btn-block mb-2" onclick="PrintPackage(${data.package}); return false;">Распечатать код поддона</button></div>`;
                data.id.forEach(function (id) {
                    products += card_to_print(id);
                })
                console.log(products);
                $("#regMany").prop("disabled", false);
                $("#cards").html(products);
            }
        })
    })
})

$('#controlBtns').delegate('#all', "click", function () {
    console.log('ass we can');
    products = "";
    $.ajax(
        {
            type: 'GET',
            url: 'http://5.200.47.32/api/positions',
            dataType: 'json',
            success: function (data, textStatus) {
                console.log(data);
                data.forEach(function (product) {
                    if (product.status == 'Departured')
                        status = 'Отгружен';
                    else if (product.status == 'In_stock')
                        status = 'На складе';
                    if (product.comment == null || product.comment == "") {
                        products = products + card_without_comm(product.id, product.mark, product.diameter, product.packing, product.part, product.plav, product.mass, status);
                    } else {
                        products = products + card(product.id, product.mark, product.diameter, product.packing, product.comment, product.part, product.plav, product.mass, status);
                    }
                });
                $('#cards').html(products);
                $('#hide').attr('class', 'btn btn-outline-info btn-block mainBtns mb-2 hide');
            }
        }
    )
})

$('#controlBtns').delegate('#openSearch', "click", function () {
    $('#cards').html(
        `<form name="searchForm" class="col-12">
         <div class="row">
            <div class="col-9 ui-widget">
               <input class="form-control col-10" name="searchValue" id="searchValue" oninput='onInput();'/>
            </div>
            <div class="col-3">
               <button type="button" class="btn btn-outline-info btn-block mb-2 search" id="search">Найти</button>
            </div>
         </div>
      </form>
      <div class="searchContent col-12 row"></div>`);
    bind_search();
})

$('#controlBtns').delegate('#openShipInput', "click", function () {
    $('#cards').html(
        `<form name="shipForm" class="col-12 shipForm">
         <div class="row">
            <div class="col-7 ui-widget">
               <input class="form-control col-10" name="shipValues" id="shipValues"/>
            </div>
            <div class="col-3">
               Вес: <span class="massInfo">0</span>
            </div>
            <div class="col-2">
               <button type="button" class="btn btn-outline-info btn-block mb-2 ship" id="ship">Отгрузить</button>
            </div>
         </div>
      </form>
      <div class="shipContent col-12 row"></div>`);
    bind_ship();
})

$('#controlBtns').delegate('#hide', 'click', function () {
    $('#cards').html('');
    $('#hide').attr('class', 'btn btn-outline-info btn-block mb-2 hide d-none');
})

$('#controlBtns').delegate("#add", "click", function () {
    console.log("dlkf ");
    $('.form').css('display', 'inline');
});

$('#controlBtns').delegate("#addMany", "click", function () {
    console.log("dlkf ");
    $('.addManyForm').css('display', 'inline');
});

$('.form').delegate('.close-block', 'click', function () {
    $('.form').fadeOut();
})

$('.addManyForm').delegate('.close-block-regMany', 'click', function () {
    $('.addManyForm').fadeOut();
})

$('#cards').delegate('#search', "click", function () {
    products = "";
    val = document.forms.searchForm.searchValue.value;
    console.log(val);
    ssyl = `http://5.200.47.32/api/search/${val}`
    $.ajax(
        {
            type: 'GET',
            url: ssyl,
            dataType: 'json',
            success: function (data, textStatus) {
                console.log(data);
                data.forEach(function (elem) {
                    if (elem.status == 'Departured')
                        status = 'Отгружен';
                    else if (elem.status == 'In_stock')
                        status = 'На складе';
                    if (elem.type == 'POSITION') {
                        if (elem.comment == null || elem.comment == "") {
                            products = products + card_without_comm(elem.id, elem.mark, elem.diameter, elem.packing, elem.part, elem.plav, elem.mass, status);
                        } else {
                            products = products + card(elem.id, elem.mark, elem.diameter, elem.packing, elem.comment, elem.part, elem.plav, elem.mass, status);
                        }
                    } else {
                        if (elem.comment == null || elem.comment == "") {
                            products = products + pack_without_comm(elem.id, elem.mark, elem.diameter, elem.packing, elem.part, elem.plav, elem.mass, status);
                        } else {
                            products = products + pack(elem.id, elem.mark, elem.diameter, elem.packing, elem.comment, elem.part, elem.plav, elem.mass, status);
                        }
                    }
                });
                $('.searchContent').html(products);
            },
            error: function () {
                console.log(data);
                profucts = data;
            }
        }
    );
    console.log(products);
})

$('#cards').delegate('#ship', 'click', function () {
    request = document.forms.shipForm.shipValues.value;
    console.log(request);
    console.log(`http://5.200.47.32:81/api/departure?request=${request}`);
    $.ajax({
        type: 'POST',
        url: `http://5.200.47.32:80/api/departure?request=${request}`,
        dataType: 'json',
        success: function (data, textStatus) {
            console.log(data);
            products = '';
            data.forEach(function (elem) {
                if (elem.status == 'Departured')
                    status = 'Отгружен';
                else if (elem.status == 'In_stock')
                    status = 'На складе';
                products += card_depart(elem.id, elem.mark, elem.diameter, elem.packing, elem.part, elem.plav, elem.mass, status);
            })
            form = `<div class="row departureForm col-12">
         <div class="col-md-12">
             <form class="departureForm text-center" name="departureForm">
                 <div class="row">
                     <label for="contrAgent">Контр. агент</label>
                     <input type="text" class="form-control" name="contrAgent" id="contrAgent">
                 </div>
                 <div class="row">
                     <label for="account">Счет</label>
                     <input type="text" class="form-control" name="account" id="account">
                 </div>
             </form>
             <button class="btn btn-outline-success btn-block" style="margin-top: 5px;" id="depart">Отгрузить</button>
         </div>
     </div><div class='cards col-12 row'>`;
            products = form + products + '</div>';
            $('#cards').html(products);
            data.forEach(function (elem) {
                console.log(elem);
                $(`#mass,#${elem.id} input`).val(`${elem.mass}`);
            })
        }
    })
})

$('#cards').delegate('#depart', 'click', function () {
    $("#depart").prop("disabled", true);
    contrAgent = document.forms.departureForm.contrAgent.value;
    account = document.forms.departureForm.account.value;
    objects = [];
    cards_mass = $('.mass');
    cards_id = $('.card');
    for (i = 0; i < cards_id.length; i++) {
        objects.push({
            "id": $(cards_id[i]).attr('id'),
            "weight": parseFloat(cards_mass[i].value.replaceAll(',', '.'))
        })
    }
    console.log(objects, contrAgent, account);
    object = {
        "data": objects,
        "contrAgent": contrAgent,
        "account": account
    }
    $.ajax({
        type: 'POST',
        url: `http://5.200.47.32:80/api/departureConfirmation`,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(object),
        dataType: 'json',
        success: function (data, textStatus) {
            window.open(`http://www.ferro-trade.ru/api/files/${data.file}`, "_new");
            console.log(data);
            objs = [];
            data.id.forEach(function (elem) {
                $.ajax({
                    type: 'GET',
                    url: `http://5.200.47.32:80/api/position/${elem}`,
                    dataType: 'json',
                    async: false,
                    success: function (data, textStatus) {
                        console.log(data);
                        objs.push(data);
                    }
                })
            })
            console.log(objs);
            products = '';
            objs.forEach(function (obj) {
                products += card_to_print(obj.id);
            })
            $("#depart").prop("disabled", false);
            $('#cards').html(products);
        }
    })
})

//$('#content').delegate('.product', 'click', function(){
//   console.log(this.id);
//   var PageLink = `product.html?id=${this.id}`;
//   var pwa = window.open(PageLink, '_blank');
//})

function CodetoPrint(id) {
    str = '';
    gost = '1';
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32/api/position/${id}`,
        dataType: 'json',
        async: false,
        success: function (data, textStatus) {
            console.log(data);
            mark = data.mark.replaceAll('–', '-');
            $.ajax({
                type: 'GET',
                url: `http://5.200.47.32/api/gost?mark=${mark}`,
                async: false,
                success: function (data, textStatus) {
                    console.log(data);
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
                "<img width='350' height='100' style='margin: auto;' src='" +
                `http://5.200.47.32/api/code/${id}` + `' />
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
        },
        error: function (data, textStatus) {
            console.log(data);
        }
    })
    console.log(str);
    return str;
}

function PrintCode(id) {
    console.log(arguments);
    prints = '';
    for (i = 0; i < arguments.length; i++) {
        prints += CodetoPrint(arguments[i]);
        console.log(prints);
    }
    var Pagelink = "";
    var pwa = window.open(Pagelink, "_blank");
    pwa.document.open();
    pwa.document.write(prints);
    pwa.document.close();
}

function PackageToPrint(id) {
    str = '';
    gost = '1';
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32/api/package?id=${id}`,
        dataType: 'json',
        async: false,
        success: function (data, textStatus) {
            console.log(data);
            mark = data.mark.replaceAll('–', '-');
            $.ajax({
                type: 'GET',
                url: `http://5.200.47.32/api/gost?mark=${mark}`,
                async: false,
                success: function (data, textStatus) {
                    console.log(data);
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
                "<img width='350' height='100' style='margin: auto;' src='" +
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
            console.log(str);
        },
        error: function (data, textStatus) {
            console.log(data);
        }
    })
    console.log(str);
    return str;
}

function PrintPackage(id) {
    console.log(arguments);
    prints = '';
    for (i = 0; i < arguments.length; i++) {
        prints += PackageToPrint(arguments[i]);
        console.log(prints);
    }
    var Pagelink = "";
    var pwa = window.open(Pagelink, "_blank");
    pwa.document.open();
    pwa.document.write(prints);
    pwa.document.close();
}

function compare() {
    if (a < b)
        return -1;
    if (a > b)
        return 1;
    return 0;
}

function sorting() {
    console.log($('#sort option:selected').val());
    sort_data = [];
    $.ajax(
        {
            type: 'GET',
            url: 'http://5.200.47.32/api/positions',
            dataType: 'json',
            async: false,
            success: function (data, textStatus) {
                console.log(data);
                switch ($('#sort option:selected').val()) {
                    case 'mark':
                        sort_data = data.sort(function (a, b) {
                            return (a.mark > b.mark) ? 1 : ((b.mark > a.mark) ? -1 : 0);
                        });
                        break;
                    case 'diameter':
                        data.sort(function (a, b) {
                            return (a.diameter > b.diameter) ? 1 : ((b.diameter > a.diameter) ? -1 : 0);
                        });
                        break;
                    case 'packing':
                        sort_data = data.sort(function (a, b) {
                            return (a.packing > b.packing) ? 1 : ((b.packing > a.packing) ? -1 : 0);
                        });
                        break;
                    case 'part':
                        sort_data = data.sort(function (a, b) {
                            return (a.part > b.part) ? 1 : ((b.part > a.part) ? -1 : 0);
                        });
                        break;
                    case 'splav':
                        sort_data = data.sort(function (a, b) {
                            return (a.plav > b.plav) ? 1 : ((b.plav > a.plav) ? -1 : 0);
                        });
                        break;
                    case 'mass':
                        sort_data = data.sort(function (a, b) {
                            return (a.mass > b.mass) ? 1 : ((b.mass > a.mass) ? -1 : 0);
                        });
                        break;
                }
                products = ``;
                data.forEach(function (product) {
                    if (product.status == 'Departured')
                        status = 'Отгружен';
                    else if (product.status == 'In_stock')
                        status = 'На складе';
                    if (product.comment == null || product.comment == "") {
                        products = products + card_without_comm(product.id, product.mark, product.diameter, product.packing, product.part, product.plav, product.mass, status);
                    } else {
                        products = products + card(product.id, product.mark, product.diameter, product.packing, product.comment, product.part, product.plav, product.mass, status);
                    }
                })
                $('#cards').html(products);
            }
        }
    )
    //console.log(sort_data);
}

$('#sort').delegate('#diameter', 'click', function () {
    console.log(this.value);
    sort(this.value);
})

function help_man() {
    input = document.forms.regForm.manufacturer.value;
    if (input == null || input == '') {
        $.ajax({
            type: 'GET',
            url: `http://5.200.47.32/api/search/manufacturer/-`,
            dataType: 'json',
            success: function (data, textStatus) {
                console.log(data);
                $("#manufacturer").autocomplete({
                    source: data
                });
                $("#manufacturerMany").autocomplete({
                    source: data
                });
            }
        })
    } else {
        $.ajax({
            type: 'GET',
            url: `http://5.200.47.32/api/search/manufacturer/${input}`,
            dataType: 'json',
            success: function (data, textStatus) {
                console.log(data);
                $("#manufacturer").autocomplete({
                    source: data
                });
                $("#manufacturerMany").autocomplete({
                    source: data
                });
            }
        })
    }
}

$(document).on("keypress", 'form', function (e) {
    var code = e.keyCode || e.which;
    if (code == 13) {
        e.preventDefault();
        return false;
    }
});

$(function () {
    var availableTags = ["Моток",
        "К300",
        "Д300",
        "К415",
        "Д415"]
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32/api/position/marks`,
        dataType: 'json',
        success: function (data, textStatus) {
            console.log(data);
            tags = '';
            data.forEach(function (tag) {
                tags += `<input name="markType" class="form-check-input" type="checkbox" id="${tag}" value="${tag}">
            <label class="form-check-label" for="${tag}">
            ${tag}
            </label><div class="w-100"></div>`;
            })
            $('.marks').html(tags);
        }
    })
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32/api/position/diameter`,
        dataType: 'json',
        success: function (data, textStatus) {
            console.log(data);
            $("#slider").slider({
                animate: "slow",
                range: true,
                min: parseFloat(data[0]),
                max: parseFloat(data[1]),
                values: [parseFloat(data[0]), parseFloat(data[1])],
                step: 0.1,
                slide: function (event, ui) {
                    $("#result-slider").text("от " + ui.values[0] + " до " + ui.values[1]);
                }
            });
            $("#result-slider").text("от " + $("#slider").slider("values", 0) + " до " + $("#slider").slider("values", 1));
        }
    })
    tags = '';
    availableTags.forEach(function (tag) {
        tags += `<input name="packingType" class="form-check-input" type="checkbox" id="${tag}" value="${tag}">
      <label class="form-check-label" for="${tag}">
      ${tag}
      </label><div class="w-100"></div>`;
    })
    $('.packing').html(tags);
})

$('.filter').delegate('#filtrate', 'click', function () {
    selected_marks = [];
    selected_packings = [];
    $("input:checkbox[name=markType]:checked").each(function () {
        selected_marks.push($(this).val());
    });
    $("input:checkbox[name=packingType]:checked").each(function () {
        selected_packings.push($(this).val());
    });
    min = $("#slider").slider("values", 0);
    max = $("#slider").slider("values", 1);
    console.log(selected_marks, selected_packings, min, max);
    object = {
        "mark": selected_marks,
        "packing": selected_packings,
        "DL": min,
        "DM": max
    }

    var settings = {
        "url": "http://5.200.47.32/api/filter",
        "method": "POST",
        "timeout": 0,
        "headers": {
            "Content-Type": "application/json"
        },
        "data": JSON.stringify(object),
    };

    $.ajax(settings).done(function (response) {
        console.log(response);
        products = '';
        response.forEach(function (product) {
            if (product.status == 'Departured')
                status = 'Отгружен';
            else if (product.status == 'In_stock')
                status = 'На складе';
            if (product.comment == '' || product.comment == null)
                products += card_without_comm(product.id, product.mark, product.diameter, product.packing, product.part, product.plav, product.mass, status);
            else
                products += card(product.id, product.mark, product.diameter, product.packing, product.comment, product.part, product.plav, product.mass, status);
        })
        $("#cards").html(products);
    });

})

function bind_search() {
    $('#searchValue').keyup(function (event) {
        elems = document.querySelectorAll('#search');
        console.log('yep');
        if (event.keyCode == 13 && elems.length != 0) {
            $('#search').click();
            $(this).val('');
        }
        return false;
    })
}

function bind_ship() {
    $('#shipValues').keyup(function (event) {
        elems = document.querySelectorAll('#ship');
        console.log('yep');
        if (event.keyCode == 13 && elems.length != 0) {
            $(this).val($(this).val() + ', ');
            $.ajax({
                type: 'GET',
                url: `http://5.200.47.32/api/departure?query=${$(this).val()}`,
                success: function (data, textStatus) {
                    console.log(data.toFixed(2));
                    $(".massInfo").html(data.toFixed(2));
                }
            })
        }
        return false;
    })
}