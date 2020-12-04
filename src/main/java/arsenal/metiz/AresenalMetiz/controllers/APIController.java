package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.assets.PositionStatus;
import arsenal.metiz.AresenalMetiz.assets.TableView;
import arsenal.metiz.AresenalMetiz.assets.WarehouseSearchView;
import arsenal.metiz.AresenalMetiz.models.SortRequest;
import arsenal.metiz.AresenalMetiz.models.WarehousePackage;
import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import arsenal.metiz.AresenalMetiz.repo.DepartureActionRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehousePackageRepo;
import arsenal.metiz.AresenalMetiz.repo.WarehouseRepo;
import org.apache.commons.math3.util.Precision;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class APIController {

    static WarehouseRepo warehouse;

    static DepartureActionRepo departure;

    static WarehousePackageRepo warehousePackage;

    static Iterable<WarehousePosition> allPositions;


    public APIController(WarehouseRepo warehouse, DepartureActionRepo departure, WarehousePackageRepo warehousePackage) {
        APIController.warehouse = warehouse;
        APIController.departure = departure;
        APIController.warehousePackage = warehousePackage;
    }


    public static void updateTags() {
        allPositions = warehouse.findAll();
    }

    public static long decodeEAN(String ean) {
        long l = Long.parseLong(ean.trim());
        long code = 0L;
        for (int i = 1; i <= 6; i++) {
            l = l / 10;
            code = code + l % 10 * (long) Math.pow(10, i);
            l = l / 10;
        }
        return code / 10;
    }

    public static float round(float value, int n) {
        return Precision.round(value, n);
    }

    private static String getAuthedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return "no info";
    }

    @GetMapping("api/positions")
    public List<WarehousePosition> getPositions() {
        updateTags();
        ArrayList<WarehousePosition> list = new ArrayList<>();
        allPositions.forEach(list::add);
        //list.removeIf(p -> p.getStatus().equals(PositionStatus.Departured));
        fix();
        return list;
    }

    @GetMapping("api/position/{id}")
    public WarehousePosition getPosition(@PathVariable("id") Long id) {
        Optional<WarehousePosition> itP = warehouse.findById(id);
        return itP.orElse(null);
    }

    @GetMapping("api/search/{query}")
    public Set<WarehouseSearchView> doSearch(@PathVariable("query") String query) {
        Set<WarehouseSearchView> set = new HashSet<>();
        Iterable<WarehousePackage> allPackages = warehousePackage.findAll();
        if (query.trim().length() == 12) {
            try {
                long finalCode = decodeEAN(query);
                System.out.println(finalCode);
                StreamSupport.stream(allPositions.spliterator(), false)
                        .filter(p -> (p.getId() == finalCode)).forEach(p -> set.add(new WarehouseSearchView(p)));
                StreamSupport.stream(allPackages.spliterator(), false)
                        .filter(p -> (p.getId() == finalCode)).forEach(p -> set.add(new WarehouseSearchView(p)));
                return set;
            } catch (Exception ignored) {
            }
        }
        StreamSupport.stream(allPositions.spliterator(), false)
                .filter(p -> (p.getMark().equalsIgnoreCase(query) || p.getPacking().equalsIgnoreCase(query)
                        || p.getDiameter().equalsIgnoreCase(query) || p.getPlav().equalsIgnoreCase(query)
                        || p.getPart().equalsIgnoreCase(query) || String.valueOf(p.getId()).equalsIgnoreCase(query)))
                .forEach(p -> set.add(new WarehouseSearchView(p)));
        StreamSupport.stream(allPackages.spliterator(), false)
                .filter(p -> (p.getMark().equalsIgnoreCase(query) || p.getPacking().equalsIgnoreCase(query)
                        || p.getDiameter().equalsIgnoreCase(query) || p.getPlav().equalsIgnoreCase(query)
                        || p.getPart().equalsIgnoreCase(query) || String.valueOf(p.getId()).equalsIgnoreCase(query)))
                .forEach(p -> set.add(new WarehouseSearchView(p)));
        return set;
    }

    @GetMapping("api/search/tag/{query}")
    public Set<String> doTagSearch(@PathVariable("query") String query) {
        Set<String> allTags = new HashSet<>();
        StreamSupport.stream(allPositions.spliterator(), false).forEach(p -> allTags.addAll(p.getAll()));
        allTags.remove(null);
        allTags.remove("null");
        if (query.trim().length() == 12) {
            System.out.println("Inside");
            try {
                long finalCode = decodeEAN(query);
                System.out.println(finalCode);
                return allTags.stream().filter(t -> t.contains(String.valueOf(finalCode))).collect(Collectors.toSet());
            } catch (Exception e) {
                return allTags.stream().filter(t -> t.contains(query)).collect(Collectors.toSet());
            }
        }
        return allTags.stream().filter(t -> t.contains(query)).collect(Collectors.toSet());
    }

    @GetMapping("api/search/manufacturer/{query}")
    public List<String> doManufacturerSearch(@PathVariable("query") String query) {
        Set<String> allTags = new HashSet<>();
        StreamSupport.stream(allPositions.spliterator(), false).forEach(p -> {
            try {
                allTags.add(p.getManufacturer());
            } catch (Exception ignored) {
            }
        });
        allTags.remove(null);
        allTags.remove("null");
        ArrayList<String> manufacturersList = new ArrayList<>(allTags);
        Collections.sort(manufacturersList);
        if (query.equals("-")) {
            return manufacturersList;
        }
        return manufacturersList.stream().filter(t -> t.contains(query)).collect(Collectors.toList());
    }

    @GetMapping("api/position/marks")
    public ArrayList<String> giveMarks() {
        updateTags();
        Set<String> allTags = new HashSet<>();
        StreamSupport.stream(allPositions.spliterator(), false).forEach(p -> {
            try {
                allTags.add(p.getMark());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        allTags.remove(null);
        allTags.remove("null");
        ArrayList<String> marks = new ArrayList<>(allTags);
        Collections.sort(marks);
        return marks;
    }

    @GetMapping("api/position/diameter")
    public ArrayList<String> giveDiameters() {
        updateTags();
        ArrayList<String> diameters = new ArrayList<>();
        ArrayList<String> diameter = new ArrayList<>();
        StreamSupport.stream(allPositions.spliterator(), false).forEach(p -> {
            try {
                diameters.add(p.getDiameter());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        diameters.remove(null);
        diameters.remove("null");
        Collections.sort(diameters);
        diameter.add(diameters.get(0));
        diameter.add(diameters.get(diameters.size() - 1));
        return diameter;
    }

    @PostMapping("/api/filter")
    public @ResponseBody
    Set<WarehousePosition> sort(@RequestBody SortRequest sortRequest) {
        Set<WarehousePosition> wp = new HashSet<>();
        boolean marksEmpty = sortRequest.getMark().isEmpty();
        boolean packingEmpty = sortRequest.getPacking().isEmpty();
        StreamSupport.stream(allPositions.spliterator(), false).forEach(p -> {
            try {
                if ((sortRequest.getMark().contains(p.getMark()) || marksEmpty) &&
                        (sortRequest.getPacking().contains(p.getPacking()) || packingEmpty)
                        && Float.parseFloat(p.getDiameter().replaceAll(",", ".")) >= sortRequest.getDL()
                        && Float.parseFloat(p.getDiameter().replaceAll(",", ".")) <= sortRequest.getDM()) {
                    wp.add(p);
                }
            } catch (Exception ignored) {
            }
        });
        wp.removeIf(p -> p.getStatus().equals(PositionStatus.Departured));
        return wp;
    }

    @PostMapping("/api/filter/table")
    public @ResponseBody
    Set<TableView> sortTable(@RequestBody SortRequest sortRequest) {
        return toTableViews(new ArrayList<>(sort(sortRequest)));
    }

    @GetMapping("api/verify")
    public void verify() {
        updateTags();
        for (WarehousePosition p : allPositions) {
            if (p.getMass() <= 0.0) {
                p.setStatus(PositionStatus.Departured);
            } else {
                p.setStatus(PositionStatus.In_stock);
            }
            warehouse.save(p);
        }
    }

    @GetMapping("api/gost")
    public String gost(@RequestParam String mark) {
        mark = mark.trim().replaceAll("–", "-");
        String[] G2246 = ("Св-08ГС,Св-12ГС,Св-08Г2С,Св-08Г2С-О,Св-08Г2С-П,Св-10ГН,Св-08ГСМТ,Св-08ГСМТ-О,Св-10ГСМТ," +
                "Св-10ГСМТ-О,Св-15ГСТЮЦА," +
                "Св-20ГСТЮА," +
                "Св-18ХГС," +
                "Св-10НМА,Св-10НМА-О,Св-08МХ,Св-08ХМ,Св-18ХМА,Св-18ХМА-О,Св-08ХНМ,Св-08ХМФА,Св-08ХМФА-О,Св-10ХМФТ," +
                "Св-08ХГ2С," +
                "Св-08ХГСМА," +
                "Св-10ХГ2СМА," +
                "Св-08ХГСМФА-О, Св-08ХГСМФА-П,Св-04Х2МА,Св-13Х2МФТ,Св-08Х3Г2СМ,Св-08ХМНФБА,Св-08ХН2М,Св-10ХН2ГМТ," +
                "Св-08ХН2ГМТА, " +
                "Св-08ХН2ГМЮ, Св-08ХН2Г2СМЮ, Св-06Н3, Св-10Х5М, Св-12X11НМФ, Св-10Х11НВМФ, Св-12Х13, Св-20Х13, " +
                "Св-06Х14, Св-08Х14ГНТ, Св-10Х17Т, Св-13Х25Т, Св-01Х19Н9, Св-08Х16Н8М2, Св-08Х18Н8Г2Б, Св-07Х18Н9ТЮ, " +
                "Св-08, Св-08А, Св-08АА, Св-08ГА, Св-10ГА, Св-10Г2, Св-06Х19Н9Т, Св-04Х19Н9С2, Св-04Х19Н9, " +
                "Св-05Х19Н9Ф3С2, Св-07Х19Н10Б, Св-08Х19Н10Г2Б, Св-06Х19Н10М3Т, Св-08Х19Н10М3Б, СВ-04Х19Н11М3, Св-04Х19Н11М3, Св-05Х20Н9ФБС, Св-06Х20Н11М3ТБ, Св-10Х20Н15, Св-07Х25Н12Г2Т, Св-06Х25Н12ТЮ, Св-07Х25Н13, Св-08Х25Н13БТЮ, Св-13Х25Н18, Св-08Х20Н9Г7Т, Св-08Х21Н10Г6, Св-30Х25Н16Г7, Св-10Х16Н25АМ6, Св-09Х16Н25М6АФ, Св-01Х23Н28М3Д3Т, Св-30Х15Н35В3Б3Т, Св-08Н50, Св-06Х15Н60М15").replaceAll(" ", "").split(",");
        List<String> G2246A = Arrays.asList(G2246);
        String[] G18143 = "12Х18Н10Т, 12Х18Н9, 08Х18Н10, 12Х13, 20Х13, 10Х17Н13М2Т, 12Х13-Т, 12Х13-Т-1".replaceAll(" ", "").split(",");
        List<String> G18143A = Arrays.asList(G18143);
        List<String> TU14_1_997_2012 = Arrays.asList("Св-ХН78Т, Св-07Х16Н6, Св-15Х18Н12С4Ю,Св-04Н3ГМТА, Св-10ГНА, Св-08ГСНТА".replaceAll(" ", "").split(","));
        List<String> TU14_1_4968_91 = Arrays.asList("Св-08Х25Н40М7, Св-08Х25Н60М10, Св-08Х25Н40М7, Св-08Х25Н60М10".replaceAll(" ", "").split(","));
        List<String> TU14_1_4345_87 = Arrays.asList("Св-10ГН1МА, Св-03ХН3МД, Св-08ГНМ, Св-08ГСНТ, Св-10ГН1МА, Св-03ХН3МД, Св-08ГНМ, Св-08ГСНТ".replaceAll(" ", "").split(","));
        List<String> TU14_130_282_96 = Arrays.asList("Св-04Н3ГМТА, Св-10ГНА, Св-08ГСНТА, Св-04Н3ГМТА, Св-10ГНА, Св-08ГСНТА".replaceAll(" ", "").split(","));

        if (G2246A.contains(mark)) {
            return "ГОСТ 2246-70";
        } else if (G18143A.contains(mark)) {
            return "ГОСТ 18143-72";
        } else if (TU14_1_997_2012.contains(mark)) {
            return "ТУ 14-1-997-2012";
        } else if (TU14_1_4968_91.contains(mark)) {
            return "ТУ 14-1-4968-91";
        } else if (TU14_1_4345_87.contains(mark)) {
            return "ТУ 14-1-4345-87";
        } else if (mark.equalsIgnoreCase("Св-04Х17Н10М2")) {
            return "ТУ 14-1-1959-77";
        } else if (mark.equalsIgnoreCase("Св-10Х19Н11М4Ф")) {
            return "ТУ 14-1-2921-80";
        } else if (mark.equalsIgnoreCase("Св-08Х19Н11Ф2С2")) {
            return "ТУ 14-1-1383-75";
        } else if (mark.equalsIgnoreCase("Св-10Х16Н25М6АФ")) {
            return "ТУ 14-1-2416-78";
        } else if (mark.equalsIgnoreCase("Св-12Х21Н5Т")) {
            return "ТУ 14-1-1464-69";
        } else if (mark.equalsIgnoreCase("Св-06ХН28МДТ")) {
            return "ТУ 14-1-1325-75";
        } else if (mark.equalsIgnoreCase("Св-10Х14Г14Н4Т")) {
            return "ТУ 14-1-2832-79";
        } else if (mark.equalsIgnoreCase("Св-08Х21Н11ФТ")) {
            return "ТУ 14-1-3638-83";
        } else if (mark.equalsIgnoreCase("Св-10НЮ")) {
            return "ТУ 14-1-2219-77";
        } else if (mark.equalsIgnoreCase("Св-10ГНМА")) {
            return "ТУ 14-1-2860-79";
        } else if (mark.equalsIgnoreCase("Св-09Х16Н4Б")) {
            return "ТУ 14-1-1692-76";
        } else if (TU14_130_282_96.contains(mark)) {
            return "ТУ 14-130-282-96";
        } else if (mark.equalsIgnoreCase("Нп-30ХГСА")) {
            return "ГОСТ 10543-98";
        } else if (mark.equalsIgnoreCase("Нп-13Х15АГ13ТЮ")) {
            return "ТУ 3-145-81";
        } else if (mark.equalsIgnoreCase("20Х25Н20С2")) {
            return "ТУ 14-1-5397-2000";
        } else if (mark.equalsIgnoreCase("20Х23Н18")) {
            return "ТУ 14-131-941-99";
        } else if (mark.equalsIgnoreCase("Св-04Х17Н10М2")) {
            return "ТУ 14-1-1959-77";
        } else if (mark.equalsIgnoreCase("Св-10Х19Н11М4Ф")) {
            return "ТУ 14-1-2921-80";
        } else if (mark.equalsIgnoreCase("Св-08Х19Н11Ф2С2")) {
            return "ТУ 14-1-1383-75";
        } else if (mark.equalsIgnoreCase("Св-10Х16Н25М6АФ")) {
            return "ТУ 14-1-2416-78";
        } else if (mark.equalsIgnoreCase("Св-12Х21Н5Т")) {
            return "ТУ 14-1-1464-69";
        } else if (mark.equalsIgnoreCase("Св-06ХН28МДТ")) {
            return "ТУ 14-1-1325-75";
        } else if (mark.equalsIgnoreCase("Св-10Х14Г14Н4Т")) {
            return "ТУ 14-1-2832-79";
        } else if (mark.equalsIgnoreCase("Св-08Х21Н11ФТ")) {
            return "ТУ 14-1-3638-83";
        } else if (mark.equalsIgnoreCase("Св-10ГНМА")) {
            return "ТУ 14-1-2860-79";
        } else if (mark.equalsIgnoreCase("Св-10НЮ")) {
            return "ТУ 14-1-2219-77";
        } else if (mark.equalsIgnoreCase("Св-09Х16Н4Б")) {
            return "ТУ 14-1-1692-76";
        } else {
            return "-";
        }
    }

    @GetMapping("api/package")
    public WarehousePackage packages(@RequestParam long id) {
        return warehousePackage.findById(id).get();
    }

    @GetMapping("api/table")
    public Set<TableView> tableView() {
        return toTableViews((List<WarehousePosition>) allPositions);
    }

    @PostMapping("api/search/plavka")
    public Set<TableView> plavkaTable(@RequestParam String plav) {

        return toTableViews(warehouse.findAllByPlav(plav));
    }

    @PostMapping("api/search/plavka/tags")
    public Set<String> plavkaAutocomplete(@RequestParam String plav) {
        return StreamSupport.stream(allPositions.spliterator(), false)
                .filter(p -> p.getPlav().contains(plav))
                .map(WarehousePosition::getPlav)
                .collect(Collectors.toSet());
    }

    private void fix() {
        Vector<WarehousePackage> packages = new Vector<>();
        warehousePackage.findAll().forEach(packages::add);
        packages.forEach(p -> {
            if (p.getMass() <= 0) {
                p.setStatus(PositionStatus.Departured);
                warehousePackage.save(p);
            }
        });
    }

    private Set<TableView> toTableViews(List<WarehousePosition> positions) {
        Set<TableView> tableViews = new TreeSet<>();
        for (WarehousePosition p : positions) {
            if (p.getStatus() == PositionStatus.In_stock) {
                TableView tableView = new TableView(p);
                if (tableViews.contains(tableView)) {
                    for (TableView tableViewFromIterator : tableViews) {
                        if (tableViewFromIterator.compareTo(tableView) == 0) {
                            tableViewFromIterator.setMass(round((float) (tableView.getMass() + tableViewFromIterator.getMass()), 2));
                        }
                    }
                } else {
                    tableViews.add(tableView);
                }
            }
        }
        tableViews.forEach(p -> p.setMass(round((float) p.getMass(), 2)));
        return tableViews;
    }

}
