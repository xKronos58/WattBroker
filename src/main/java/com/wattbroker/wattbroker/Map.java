package com.wattbroker.wattbroker;

import com.util.util;
import com.util.util.Vector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Random;

public class Map extends AnchorPane {

    Color StartColour = Color.rgb(129, 207, 252);
    Color endColour = Color.rgb(82,91, 195);
    double minValue = 0.0;
    double maxValue = 100.0; /*TODO base of actual prices*/
    @FXML @SuppressWarnings("unused")
    private AnchorPane map_root;
    @FXML @SuppressWarnings("unused")
    private Text hourButton;
    @FXML @SuppressWarnings("unused")
    private Text dayButton;
    @FXML @SuppressWarnings("unused")
    private Text weekButton;
    @FXML @SuppressWarnings("unused") private Text _1;
    @FXML @SuppressWarnings("unused") private Text _2;
    @FXML @SuppressWarnings("unused") private Text _3;
    @FXML @SuppressWarnings("unused") private Text _4;
    @FXML @SuppressWarnings("unused") private Text _5;
    @FXML @SuppressWarnings("unused") private Text _6;
    @FXML @SuppressWarnings("unused") private Text _7;
    Group root;
    Random r = new Random();
    boolean[] onOverlay = {
            false,
            false,
            false,
            false,
            false,
            false
    };

    /** Avoid magic numbers for array index */
    byte SA = 0, NT = 1, VIC = 2, NSW = 3, TAS = 4, QLD = 5;
    private final double[] prices = new double[] {
            r.nextDouble(100),
            r.nextDouble(100),
            r.nextDouble(100),
            r.nextDouble(100),
            r.nextDouble(100),
            r.nextDouble(100)
    };
    private final StackPane[] Overlays = new StackPane[] {
            CreateOverlay("SA"),
            CreateOverlay("NT"),
            CreateOverlay("VIC"),
            CreateOverlay("NSW"),
            CreateOverlay("TAS"),
            CreateOverlay("QLD")
    };

    private StackPane CreateOverlay(String state) {
        SVGPath body = new SVGPath(), triangle = new SVGPath();
        body.setContent("M0 20.7496C0 9.28993 9.40202 0 21 0H124C135.598 0 145 9.28993 145 20.7496C145 32.2093 135.598 41.4993 124 41.4993H21C9.40205 41.4993 0 32.2093 0 20.7496Z");
        triangle.setContent("M29.3135 29.6423L40.6272 40.8212L29.3135 52L17.9998 40.8212L29.3135 29.6423Z");

        Text p = getText(switch (state.toUpperCase()) {
            case "SA" -> prices[SA];
            case "NT" -> prices[NT];
            case "VIC" -> prices[VIC];
            case "NSW" -> prices[NSW];
            case "TAS" -> prices[TAS];
            case "QLD" -> prices[QLD];
            default -> -0;
        });
        StackPane full = new StackPane(new Group(body, triangle), p);
        switch (state.toUpperCase()){
            case "SA" :
                full.setTranslateY(400);
                full.setTranslateX(400);
                full.setId("icon_sa");
                full.hoverProperty().addListener(e -> onOverlay[SA] = true);
                full.onMouseExitedProperty().set(e -> onOverlay[SA] = false);
                break;
            case "NT" :
                full.setTranslateY(200);
                full.setTranslateX(400);
                full.setId("icon_nt");
                full.hoverProperty().addListener(e -> onOverlay[NT] = true);
                full.onMouseExitedProperty().set(e -> onOverlay[NT] = false);
                break;
            case "VIC" :
                full.setTranslateY(600);
                full.setTranslateX(600);
                full.setId("icon_vic");
                full.hoverProperty().addListener(e -> onOverlay[VIC] = true);
                full.onMouseExitedProperty().set(e -> onOverlay[VIC] = false);
                break;
            case "NSW" :
                full.setTranslateY(400);
                full.setTranslateX(600);
                full.setId("icon_nsw");
                full.hoverProperty().addListener(e -> onOverlay[NSW] = true);
                full.onMouseExitedProperty().set(e -> onOverlay[NSW] = false);
                break;
            case "TAS" :
                full.setTranslateY(800);
                full.setTranslateX(600);
                full.setId("icon_tas");
                full.hoverProperty().addListener(e -> onOverlay[TAS] = true);
                full.onMouseExitedProperty().set(e -> onOverlay[TAS] = false);
                break;
            case "QLD" :
                full.setTranslateY(200);
                full.setTranslateX(600);
                full.setId("icon_qld");
                full.hoverProperty().addListener(e -> onOverlay[QLD] = true);
                full.onMouseExitedProperty().set(e -> onOverlay[QLD] = false);
                break;
        }
        return full;
    }

    private Text getText(double state) {
        Text p = new Text(Math.round(
                state
        ) + "MW/H");

        p.setFill(Color.WHITE);
        p.setTranslateY(-5);
        p.setFont(new Font(18));
        return p;
    }

    public Map() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Map.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        System.out.println(this.getParent());

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Random r = new Random();

        SVGPath[] states = new SVGPath[] {
                WA(),
                SA(prices[SA]),
                NT(prices[NT]),
                VIC(prices[VIC]),
                NSW(prices[NSW]),
                TAS(prices[TAS]),
                QLD(prices[QLD])
        };

        root = new Group(states);
        root.setScaleX(0.5);
        root.setScaleY(0.5);
        root.setTranslateY(-230);
        root.setTranslateX(-200);
        map_root.getChildren().add(root);

        Line l = new Line(77, 24, 77+ dayButton.prefWidth(0), 24);
        l.setStroke(Color.rgb(120, 32, 150, 1));
        l.setStrokeWidth(3);
        map_root.getChildren().add(l);

        dayButton.setFill(Color.WHITE);
        final char[] lastMap = {'d'}; // [h, d, w] => hour graph, day graph, week graph. Default is day graph

        // GET text position for resizable graphs

        hourButton.setOnMouseClicked(e -> {
            // Ignores if current
            if(lastMap[0] == 'h') return;
            // Update Graph
//            updateGraph(new char[]{'h'});
            // Set Buttons
            hourButton.setFill(Color.WHITE);
            l.setStartX(hourButton.getX());
            l.setEndX(hourButton.prefWidth(0));
            l.setTranslateX(0);
            dayButton.setFill(Color.rgb(132, 132, 132, 1));
            weekButton.setFill(Color.rgb(132, 132, 132, 1));
            // Set Time
            _1.setText("00:00");
            _2.setText("10.00");
            _3.setText("20.00");
            _4.setText("30.00");
            _5.setText("40.00");
            _6.setText("50.00");
            _7.setText("60.00");
            // Set last graph
            lastMap[0] = 'h';
        });
        dayButton.setOnMouseClicked(e -> {
            // Ignores if current
            if(lastMap[0] == 'd') return;
            // Update Graph
//            updateGraph(new char[]{'d'});
            // Set Buttons
            dayButton.setFill(Color.WHITE);
            l.setStartX(dayButton.getX());
            l.setEndX(dayButton.prefWidth(0));
            l.setTranslateX(77);
            hourButton.setFill(Color.rgb(132, 132, 132, 1));
            weekButton.setFill(Color.rgb(132, 132, 132, 1));
            // Set Time
            _1.setText("00:00");
            _2.setText("04.00");
            _3.setText("08.00");
            _4.setText("12.00");
            _5.setText("16.00");
            _6.setText("20.00");
            _7.setText("24.00");
            // Set last graph
            lastMap[0] = 'd';
        });
        weekButton.setOnMouseClicked(e -> {
            // Ignores if current
            if (lastMap[0] == 'w') return;
            // Update Graph
//            updateGraph(new char[]{'w'});
            // Set Buttons
            weekButton.setFill(Color.WHITE);
            l.setStartX(weekButton.getX());
            l.setEndX(weekButton.prefWidth(0));
            l.setTranslateX(135);
            hourButton.setFill(Color.rgb(132, 132, 132, 1));
            dayButton.setFill(Color.rgb(132, 132, 132, 1));
            // Set Time
            _1.setText("MON    ");
            _2.setText("TUE    ");
            _3.setText("WED    ");
            _4.setText("THU    ");
            _5.setText("FIR    ");
            _6.setText("SAT    ");
            _7.setText("SUN    ");
            // Set last graph
            lastMap[0] = 'w';
        });
    }

    private SVGPath WA() {
        SVGPath svg = new SVGPath();
        String path = "M467.086 131.602L447.006 127.833L438.848 125.321L419.395 107.735L408.728 103.339L400.57 98.9424H393.668L388.648 102.711L390.53 108.991L386.138 111.504L383.628 108.991L369.822 114.016L364.175 128.461L357.272 124.065L342.84 130.974L337.192 139.138L344.095 146.675H342.212L336.565 148.559L337.82 159.864L329.662 157.98L319.622 166.145L317.739 171.17L318.994 177.45L322.132 182.475L316.484 189.383L318.367 194.408L306.444 193.152L298.914 191.268L293.267 193.152L295.149 198.176L292.639 206.341L302.052 216.39L300.169 222.671L296.404 218.902L293.894 236.488C293.894 236.488 287.619 233.348 285.737 230.836C283.854 228.323 273.814 205.713 273.814 205.713V202.573L269.421 205.085L256.871 220.159L249.341 229.579L248.086 239.628L251.851 250.306L255.616 259.098L251.224 266.007L238.674 277.312L231.771 300.551L223.613 311.228L204.161 322.533L197.886 327.557L180.943 331.326L175.923 335.722L158.353 331.954L152.705 342.631L149.568 345.143L130.742 350.168L126.35 353.936L121.957 361.473L109.407 365.869L96.2296 363.985L85.562 365.241L76.777 370.894L63.5993 385.339L51.6767 400.413L34.1065 409.206L27.204 415.486L24.0664 435.584L18.4189 438.724H14.0263L11.5163 419.255L6.49625 420.511L0.221191 445.005L7.75127 455.682L8.37877 462.591L6.49625 479.549L2.10371 490.226V507.183L10.8888 516.604L16.5364 529.166L29.714 543.611L37.8716 553.66L38.4991 568.734L29.0865 563.709L11.5163 545.495L9.63379 553.66L20.3014 564.337L26.5765 573.13L12.1438 571.246L14.6538 576.898L34.734 598.881L42.2641 612.07L44.1466 622.747L64.2268 646.613L74.8944 662.315L78.032 679.273L78.6595 689.322L86.1895 705.651L105.015 732.658L109.407 743.335L112.545 770.97L114.427 785.415L113.8 793.58L110.662 804.257L106.27 805.513L96.8572 801.745V808.654L98.7397 823.727L102.505 830.008L109.407 827.495L122.585 831.892L133.253 841.941L142.665 845.709L160.235 846.337L177.805 844.453L196.003 837.544L201.651 832.52L210.436 820.587L219.848 817.446L226.751 814.306L228.633 804.257L240.556 797.348L267.539 792.952L284.482 789.812L298.914 792.952L322.759 788.556H327.152L332.799 789.812L344.095 784.159L351.625 773.482L354.762 757.152L359.155 754.012L371.705 751.5L407.473 728.262L418.768 726.377L442.613 727.633L455.791 720.097L477.126 708.792L488.421 703.767L467.086 134.742V131.602Z";
        svg.setContent(path);
        svg.setFill(Color.DARKGRAY);
        return svg;
    }

    private SVGPath SA(double price) {
        SVGPath svg = new SVGPath();
        String path = "M818.175 923.277L809.39 922.021L802.801 914.484L790.878 892.816L788.368 884.651L790.878 878.371L787.741 864.553L785.544 851.05L777.073 843.513L767.974 835.976L761.072 836.918L756.993 838.175L748.835 837.232L749.463 833.15L757.307 819.961L758.875 813.052L758.562 806.143L748.208 789.814L746.012 796.094L739.109 819.647L733.775 822.159H719.029L715.891 814.936L719.342 812.424L727.186 812.11L728.441 807.085L730.324 790.756L733.775 778.195L742.56 770.658L744.757 766.889L744.443 756.84L748.522 754.642L749.777 753.072L742.56 732.974L739.736 733.602L739.109 744.907L734.716 749.618L726.873 763.121L723.108 770.658L715.577 771.914L704.282 779.765L695.811 792.012L688.595 803.631L681.378 808.97L674.476 806.143L667.259 781.963L666.946 775.682L658.161 767.203L652.199 753.7L644.355 750.874L638.708 745.221L636.198 740.511L639.963 733.602L633.688 730.148L630.55 725.437L626.472 718.843L620.51 716.016L610.784 719.157L605.764 715.388L599.175 711.934L592.586 710.678L589.449 713.19H584.429L576.899 707.851L565.603 700.943L555.877 696.232L552.426 695.604L549.602 696.232L544.896 698.745L530.149 699.059L493.754 701.571L485.91 515.036L599.489 512.838L688.908 513.152L780.838 516.92L843.589 519.433L820.057 923.905L818.175 923.277Z";
        svg.setContent(path);
        final Boolean[] overlayAdded = {(Boolean) false}; // Note the IDE is having a stroke and apparently Boolean != boolean (true)

        svg.setFill(interpolateColor(
                StartColour, endColour,
                normalise(price, minValue, maxValue)));

        // Set listener for state hover
        svg.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if(!overlayAdded[0] && !onOverlay[SA]) {
                map_root.getChildren().add(Overlays[SA]);
                overlayAdded[0] = (Boolean) true;
            }
        });
        svg.setOnMouseExited(e -> {
            if(onOverlay[SA])
                return;
            map_root.getChildren().removeIf(n -> n.getId() != null && n.getId().equals("icon_sa"));
            overlayAdded[0] = (Boolean) false;
        });
        svg.setOnMouseClicked(e -> {
            StateOverlay so = new StateOverlay(State.SA);
            so.Build(Login.primaryStage);
        });


        return svg;
    }

    private void addHoverItem(double price, Vector<Double, Double> pos, String state) {
        SVGPath body = new SVGPath();
        body.setContent("M0 20.7496C0 9.28993 9.40202 0 21 0H124C135.598 0 145 9.28993 145 20.7496C145 32.2093 135.598 41.4993 124 41.4993H21C9.40205 41.4993 0 32.2093 0 20.7496Z");
        SVGPath triangle = new SVGPath();
        triangle.setContent("M29.3135 29.6423L40.6272 40.8212L29.3135 52L17.9998 40.8212L29.3135 29.6423Z");
        Text p = getText(price);
        Group icon = new Group(body, triangle);
        StackPane full = new StackPane(icon, p);
        full.setTranslateX(pos.getX());
        full.setTranslateY(pos.getY());
        full.setId("icon");
        map_root.getChildren().add(full);
    }

    private SVGPath NT(double price) {
        SVGPath svg = new SVGPath();
        String path = "M472.734 136.628L482.147 134.744L491.56 137.256L492.187 129.091L482.774 119.67L482.147 113.39L485.912 106.481L492.815 105.853L496.58 86.383L502.227 88.2672L509.13 79.4743L505.365 71.9375V65.6569H511.012L512.895 60.0043L520.425 55.6079L521.68 46.815H531.092L536.112 39.9063L549.29 43.6747L563.095 41.7905L570.625 44.3027L581.293 38.0221L590.706 35.5099L588.196 22.9486L581.92 17.296L573.135 16.0399L566.86 9.13122L581.92 2.85059L594.471 16.668L600.746 12.2715L608.276 24.2047L623.336 27.9731L631.493 23.5767L632.748 29.2292L660.359 34.2537L664.124 34.8818L673.536 42.4186L678.556 43.0466L683.576 38.0221L692.362 39.2782L694.244 43.6747L702.402 41.7905L706.167 49.9553L711.814 50.5834L716.207 44.3027L711.814 39.2782L720.599 34.8818H722.482L726.247 40.5344L730.639 41.7905L733.777 43.0466L736.914 48.0711L730.639 54.9798L726.247 61.8885L721.227 62.5166L722.482 70.6814L718.717 76.334L713.069 74.4498L700.519 82.6146V93.2917L704.284 97.06L698.637 107.109L699.264 110.249L693.617 114.646L686.086 127.207L683.576 129.091L686.086 137.884L709.932 153.586L712.442 158.61L723.109 164.263L725.619 172.428L737.542 170.543L750.092 178.708L760.132 183.733L765.152 193.154L753.857 510.326L692.362 508.441L615.178 507.185L512.895 510.326L485.284 510.954L472.734 136.628Z";
        svg.setContent(path);
        svg.setFill(interpolateColor(
                StartColour, endColour,
                normalise(price, minValue, maxValue)));
        boolean[] overlayAdded = new boolean[] {false};
        // Set listener for state hover
        svg.hoverProperty().addListener(listener -> {
            if(!overlayAdded[0]) {
                map_root.getChildren().add(Overlays[NT]);
                overlayAdded[0] = true;
            }
        });
        svg.setOnMouseExited(e -> {
            map_root.getChildren().removeIf(n -> n.getId() != null && n.getId().equals("icon_nt"));
            overlayAdded[0] = false;
        });

        svg.setOnMouseClicked(e -> {
            System.out.println("nt");
        });

        return svg;
    }

    private SVGPath QLD(double price) {
        SVGPath svg = new SVGPath();
        String path = "M770.798 196.294L780.211 199.434L794.016 204.458L801.546 210.111L807.821 223.3L819.116 227.697L831.039 237.118L841.706 234.605L852.374 233.349L859.276 223.928L866.807 210.739L874.964 200.062L880.612 181.22L881.867 169.287L890.024 147.933L886.259 137.884L889.397 120.926L887.514 113.389L884.377 105.852L889.397 96.4315L894.417 88.8948L893.162 75.0774L899.437 71.9371L903.829 66.2845L895.672 58.7478L909.477 37.3936L914.497 16.0395L916.379 10.3869L923.282 6.61852L929.557 0.337891L932.695 1.59402L932.067 5.99046L939.597 12.2711L940.225 39.2778L943.99 43.6742L949.01 46.8146L943.99 55.6074L954.657 68.1687V73.1932L957.795 82.6142L955.912 105.224L960.305 120.926L962.815 132.231L970.345 136L978.503 128.463L988.543 126.579L991.053 137.256L1004.23 148.561L1010.51 153.585L1009.88 171.799L1013.02 186.873L1012.39 196.294L1011.13 205.086L1025.57 235.233L1029.96 243.398L1029.33 257.216L1024.94 267.265L1033.1 270.405L1031.21 285.479L1030.59 291.759L1039.37 302.436L1052.55 311.229L1065.73 312.485L1068.24 320.022L1070.75 328.815L1085.18 331.327L1091.45 344.516L1097.1 340.12L1107.14 349.541L1104 357.078L1110.91 372.151L1117.81 387.853L1120.32 411.719V424.28L1129.1 426.165L1131.61 428.677L1132.87 414.86L1138.52 418.628L1152.32 433.701L1157.97 443.75L1154.83 463.848L1169.89 484.575L1178.68 486.459L1184.32 495.252L1186.21 505.929L1197.5 514.094L1200.01 522.258L1203.15 530.423L1208.17 534.82L1207.54 545.497L1212.56 554.29L1211.31 558.686L1207.54 564.339L1208.17 588.833L1207.54 595.114L1202.52 594.486L1205.66 602.65L1211.31 615.212L1210.05 630.913L1194.99 632.169L1182.44 627.145L1163.62 637.194L1162.36 646.615L1154.83 645.987L1151.69 645.359L1146.05 652.267L1142.91 652.895L1144.16 646.615L1139.77 641.59L1136.63 637.194L1128.48 634.054H1120.32L1116.55 627.773L1101.49 630.285L1092.08 625.261L1083.3 628.401L1074.51 637.194L975.993 627.773L922.027 621.492L848.609 616.468L841.706 615.84L848.609 515.35L759.503 510.953L770.798 196.294Z";
        svg.setContent(path);
        svg.setFill(interpolateColor(
                StartColour, endColour,
                normalise(price, minValue, maxValue)));
        boolean[] overlayAdded = new boolean[] {false};
        // Set listener for state hover
        svg.hoverProperty().addListener(listener -> {
            if(!overlayAdded[0]) {
                map_root.getChildren().add(Overlays[QLD]);
                overlayAdded[0] = true;
            }
        });
        svg.setOnMouseExited(e -> {
            map_root.getChildren().removeIf(n -> n.getId() != null && n.getId().equals("icon_qld"));
            overlayAdded[0] = false;
        });

        svg.setOnMouseClicked(e -> {
            System.out.println("qld");
        });
        return svg;
    }

    private SVGPath NSW(double price) {
        SVGPath svg = new SVGPath();
        String path = "M842.334 620.863L933.95 628.4L1001.09 634.681L1076.39 642.845L1085.81 633.425L1093.34 631.54L1099.61 633.425L1112.79 632.796L1117.18 635.937L1120.95 637.193L1127.85 638.449L1132.24 641.589L1135.38 644.102L1139.77 649.126L1140.4 655.407L1144.79 656.663L1149.81 654.151L1152.95 648.498L1158.6 651.638H1165.5L1168.01 645.358L1167.38 639.705L1182.44 632.796L1189.35 634.681L1195.62 637.821L1210.68 634.681L1210.05 651.01L1201.9 666.084L1194.99 685.554L1186.83 703.768V715.073L1181.19 728.262L1174.28 743.336L1168.01 750.872L1162.99 758.409L1159.85 767.202L1137.26 781.647L1119.69 802.374L1114.04 816.191L1107.14 828.752L1101.49 847.594L1093.34 855.759L1082.04 867.692L1075.77 883.394L1070.75 898.467L1066.98 921.077L1063.84 924.846L1022.43 897.839L1023.06 888.418L1020.55 882.138V871.46L1009.25 866.436L996.073 868.32L984.15 865.808L969.718 865.18L952.775 859.527L942.108 855.131L932.695 857.015L932.067 860.783L932.695 864.552L930.812 865.18L926.42 862.668L919.517 851.362L911.987 843.198L901.947 837.545L898.81 831.892L894.417 826.868L896.927 816.191L890.652 813.365L879.043 809.596L873.709 813.679L869.317 803.63L868.689 797.977L856.767 789.812L851.119 787.928L845.472 791.068L832.294 786.044L842.334 620.863Z";
        svg.setContent(path);
        svg.setFill(interpolateColor(
                StartColour, endColour,
                normalise(price, minValue, maxValue)));
        boolean[] overlayAdded = new boolean[] {false};
        // Set listener for state hover
        svg.hoverProperty().addListener(listener -> {
            if(!overlayAdded[0]) {
                map_root.getChildren().add(Overlays[NSW]);
                overlayAdded[0] = true;
            }
        });
        svg.setOnMouseExited(e -> {
            map_root.getChildren().removeIf(n -> n.getId() != null && n.getId().equals("icon_nsw"));
        });

        svg.setOnMouseClicked(e -> {
            System.out.println("nsw");
        });
        return svg;
    }
    private SVGPath VIC(double price) {
        SVGPath svg = new SVGPath();
        String path = "M831.04 791.068L823.51 926.102L838.57 936.779L846.1 931.754L856.768 936.151L873.083 944.944L886.888 954.993L913.871 942.432L919.518 937.407L927.676 925.474L931.441 932.383L927.676 941.175L926.421 944.944L941.481 953.737L950.266 959.389L952.776 963.786L976.621 958.761L996.074 941.175L1013.02 935.523L1034.35 934.895L1053.8 936.151L1061.33 928.614L1018.66 900.351L1016.78 895.327L1018.66 887.79L1016.15 885.278L1014.9 875.229L1009.25 873.345L1001.72 872.088L996.074 874.601L987.916 872.088L982.269 869.576L974.739 872.717L969.091 870.204L950.894 863.924L944.618 861.411H937.088L935.833 868.32L932.068 870.204L926.107 869.576L920.146 860.155L915.126 851.362L905.713 843.826L896.928 840.685L892.222 832.206L891.594 823.1L889.712 816.819L883.437 814.935L878.103 813.679L873.083 818.075L868.063 810.538L864.925 806.142V800.489L854.258 794.209L848.61 796.093L841.707 794.837L831.04 791.068Z";
        svg.setContent(path);
        svg.setFill(interpolateColor(
                StartColour, endColour,
                normalise(price, minValue, maxValue)));
        boolean[] overlayAdded = new boolean[] {false};
        // Set listener for state hover
        svg.hoverProperty().addListener(listener -> {
            if(!overlayAdded[0]) {
                map_root.getChildren().add(Overlays[VIC]);
                overlayAdded[0] = true;
            }
        });
        svg.setOnMouseExited(e -> {
            map_root.getChildren().removeIf(n -> n.getId() != null && n.getId().equals("icon_vic"));
            overlayAdded[0] = false;
        });

        svg.setOnMouseClicked(e -> {
            System.out.println("vic");
        });
        return svg;
    }
    private SVGPath TAS(double price) {
        SVGPath svg = new SVGPath();
        String path = "M958.424 1127.4L964.385 1117.35L969.405 1115.47L975.994 1102.59H978.818L986.348 1103.85L990.113 1099.14L995.447 1080.92L1001.72 1074.95L1003.29 1060.82L1003.92 1048.89L1006.74 1040.73C1006.74 1040.73 1000.47 1031.93 999.212 1031.93C997.957 1031.93 990.113 1037.27 990.113 1037.27L973.798 1037.59L966.895 1039.47L955.6 1042.3L936.147 1032.87L916.067 1022.51L913.243 1022.83L911.361 1029.42L909.792 1036.02L914.498 1050.77L919.205 1061.77L924.538 1071.81L926.735 1080.61L924.852 1086.57L924.538 1091.28L933.637 1111.38L938.344 1122.06L942.109 1123.94L949.011 1123.32L955.6 1127.4H958.424Z";
        svg.setContent(path);
        svg.setFill(interpolateColor(
                StartColour, endColour,
                normalise(price, minValue, maxValue)));
        boolean[] overlayAdded = new boolean[] {false};
        // Set listener for state hover
        svg.hoverProperty().addListener(listener -> {
            if(!overlayAdded[0]) {
                map_root.getChildren().add(Overlays[TAS]);
                overlayAdded[0] = true;
            }
        });
        svg.setOnMouseExited(e -> {
            map_root.getChildren().removeIf(n -> n.getId() != null && n.getId().equals("icon_tas"));
            overlayAdded[0] = false;
        });

        svg.setOnMouseClicked(e -> {
            System.out.println("tas");
        });
        return svg;
    }
    /**
     * Finds the minimum and maximum of a set of values then returns a vector
     * with min as the X and max as the Y
     * @param values set of values to be searched
     * @return util.Vector\<\Double, Double\>\ X -> min | Y -> max
     * */
    private Vector<Double, Double> getMinMax(double... values) {
        double min = values[0], max = values[0];
        for(double d : values) {
            if(d < min) min = d;
            else if(d > max) max = d; // Else as a value cant be min and max.
        }
        return new Vector<>(min, max);
    }
    /**
     * Takes the price of the state and turns it into a point along the gradient.
     * @param val price
     * @param minValue minimum value for the gradient (Lowest state price)
     * @param maxValue maximum value for the gradient (Highest state price)
     * maxValue and minValue should be the lowest and highest state prices to give an even colour distribution*/
    private double normalise(double val, double minValue, double maxValue) {
        return (val - minValue) / (maxValue - minValue);
    }

    /**
     * Takes the fraction from the normalise() method and then the colour schemes
     * high and low gradient and returns a colour along that gradient
     * @param startColor low colour of the gradient for poor prices
     * @param endColor high colour of the gradient for the good prices
     * @param fraction value from the normalise() method.
     * */
    private Color interpolateColor(Color startColor, Color endColor, double fraction) {
        double red = startColor.getRed() + fraction * (endColor.getRed() - startColor.getRed());
        double green = startColor.getGreen() + fraction * (endColor.getGreen() - startColor.getGreen());
        double blue = startColor.getBlue() + fraction * (endColor.getBlue() - startColor.getBlue());
        return new Color(red, green, blue, 1.0);
    }

    static class Overlay extends StackPane {

        StackPane item;
        Text p = new Text(0 + "MW/H");
        public Overlay() {
            this.item = BuildOverlay();
        }
        private StackPane BuildOverlay() {
            SVGPath body = new SVGPath();
            body.setContent("M0 20.7496C0 9.28993 9.40202 0 21 0H124C135.598 0 145 9.28993 145 20.7496C145 32.2093 135.598 41.4993 124 41.4993H21C9.40205 41.4993 0 32.2093 0 20.7496Z");
            SVGPath triangle = new SVGPath();
            triangle.setContent("M29.3135 29.6423L40.6272 40.8212L29.3135 52L17.9998 40.8212L29.3135 29.6423Z");
            p.setFill(Color.WHITE);
            p.setTranslateY(-5);
            p.setFont(new Font(18));
            p.setId("text");
            Group icon = new Group(body, triangle);
            return new StackPane(icon, p);
        }

    }

    public enum State {
        SA{
            @Override
            public String getPath() {
                return "M243.923 300.756L237.474 299.835L232.637 294.312L223.884 278.435L222.041 272.452L223.884 267.85L221.581 257.725L219.968 247.83L213.749 242.307L207.07 236.785L202.002 237.475L199.008 238.395L193.019 237.705L193.48 234.714L199.238 225.049L200.39 219.986L200.16 214.924L192.559 202.958L190.946 207.56L185.879 224.819L181.963 226.66H171.138L168.834 221.367L171.368 219.526L177.126 219.296L178.048 215.614L179.43 203.649L181.963 194.444L188.413 188.921L190.025 186.16L189.795 178.797L192.789 177.186L193.71 176.035L188.413 161.308L186.34 161.768L185.879 170.052L182.654 173.504L176.896 183.399L174.132 188.921L168.604 189.842L160.312 195.595L154.093 204.569L148.795 213.083L143.498 216.995L138.43 214.924L133.133 197.205L132.902 192.603L126.453 186.39L122.077 176.495L116.318 174.424L112.172 170.282L110.33 166.831L113.094 161.768L108.487 159.237L106.184 155.785L103.189 150.953L98.813 148.882L91.6727 151.183L87.9873 148.422L83.1503 145.891L78.3133 144.97L76.01 146.811H72.3247L66.7967 142.899L58.5047 137.837L51.3643 134.385L48.8307 133.925L46.7577 134.385L43.3027 136.226L32.477 136.456L5.75833 138.297L0 1.61078L83.3807 0L149.026 0.230111L216.513 2.99145L262.58 4.83234L245.305 301.216L243.923 300.756Z";
            }

            @Override
            public Vector<Double, Double> getSVGLocation() {
                return new Vector<>(52.0, 213.0);
            }

            @Override
            public String getName() {
                return "SOUTH AUSTRALIA";
            }
        }, NT{
            @Override
            public String getPath() {
                return "M0 119.905L8.45215 118.216L16.9043 120.468L17.4678 113.15L9.01562 104.706L8.45215 99.0763L11.833 92.884L18.0312 92.3211L21.4121 74.8701L26.4834 76.5589L32.6816 68.6779L29.3008 61.9227V56.2933H34.3721L36.0625 51.2269L42.8242 47.2864L43.9512 39.4053H52.4033L56.9111 33.2131L68.7441 36.5907L81.1406 34.9019L87.9023 37.1536L97.4814 31.5243L105.934 29.2725L103.68 18.0139L98.0449 12.9475L90.1562 11.8216L84.5215 5.62933L98.0449 0L109.314 12.3845L114.949 8.444L121.711 19.1397L135.234 22.5173L142.56 18.5768L143.686 23.6432L168.479 28.1467L171.86 28.7096L180.312 35.4648L184.82 36.0277L189.328 31.5243L197.217 32.6501L198.907 36.5907L206.232 34.9019L209.613 42.22L214.685 42.7829L218.629 37.1536L214.685 32.6501L222.573 28.7096H224.264L227.644 33.776L231.589 34.9019L234.406 36.0277L237.224 40.5312L231.589 46.7235L227.644 52.9157L223.137 53.4787L224.264 60.7968L220.883 65.8632L215.811 64.1744L204.542 71.4925V81.0624L207.923 84.44L202.852 93.4469L203.415 96.2616L198.344 100.202L191.582 111.461L189.328 113.15L191.582 121.031L212.994 135.104L215.248 139.607L224.827 144.674L227.081 151.992L237.787 150.303L249.057 157.621L258.072 162.125L262.58 170.569L252.437 454.85L197.217 453.161L127.909 452.036L36.0625 454.85L11.2695 455.413L0 119.905Z";
            }

            @Override
            public Vector<Double, Double> getSVGLocation() {
                return new Vector<>(60.0, 175.0);
            }

            @Override
            public String getName() {
                return "NORTHERN TERRITORY";
            }
        },  QLD{
            @Override
            public String getPath() {
                return "M6.54629 113.36L12.0015 115.176L20.0026 118.083L24.3668 121.353L28.0036 128.983L34.5499 131.526L41.4599 136.976L47.6425 135.523L53.8251 134.796L57.8256 129.346L62.1898 121.716L66.9177 115.54L70.1908 104.64L70.9182 97.7364L75.646 85.3831L73.4639 79.5698L75.2824 69.7598L74.1913 65.3998L72.3729 61.0398L75.2824 55.5899L78.1918 51.2299L77.4645 43.2366L81.1013 41.4199L83.6471 38.1499L78.9192 33.7899L86.9202 21.4366L89.8297 9.08331L90.9207 5.81332L94.9212 3.63332L98.5581 0L100.376 0.726665L100.013 3.26999L104.377 6.90332L104.741 22.5266L106.923 25.0699L109.832 26.8866L106.923 31.9733L113.105 39.2399V42.1466L114.924 47.5965L113.833 60.6765L116.379 69.7598L117.833 76.2998L122.197 78.4798L126.925 74.1198L132.744 73.0298L134.199 79.2065L141.836 85.7464L145.473 88.6531L145.109 99.1897L146.928 107.91L146.564 113.36L145.837 118.446L154.202 135.886L156.747 140.61L156.384 148.603L153.838 154.416L158.566 156.233L157.475 164.953L157.111 168.586L162.203 174.763L169.84 179.85L177.477 180.576L178.932 184.936L180.387 190.023L188.751 191.476L192.388 199.106L195.661 196.563L201.48 202.013L199.662 206.373L203.662 215.093L207.663 224.176L209.118 237.983V245.249L214.209 246.339L215.664 247.793L216.391 239.799L219.664 241.979L227.665 250.699L230.939 256.513L229.12 268.139L237.849 280.129L242.94 281.219L246.213 286.306L247.304 292.483L253.851 297.206L255.305 301.929L257.124 306.653L260.033 309.196L259.67 315.373L262.579 320.459L261.852 323.003L259.67 326.272L260.033 340.442L259.67 344.076L256.76 343.712L258.579 348.436L261.852 355.702L261.124 364.786L252.396 365.512L245.122 362.606L234.212 368.419L233.484 373.869L229.12 373.506L227.302 373.142L224.029 377.139L222.21 377.502L222.938 373.869L220.392 370.962L218.573 368.419L213.846 366.602H209.118L206.936 362.969L198.207 364.422L192.752 361.516L187.66 363.332L182.569 368.419L125.471 362.969L94.1939 359.336L51.643 356.429L47.6425 356.066L51.643 297.933L0 295.389L6.54629 113.36Z";
            }

            @Override
            public Vector<Double, Double> getSVGLocation() {
                return new Vector<>(60.0, 175.0);
            }

            @Override
            public String getName() {
                return "QUEENSLAND";
            }
        }, NSW{
            @Override
            public String getPath() {
                return "M6.9673 0L70.5439 5.22044L117.138 9.5708L169.392 15.2263L175.924 8.70073L181.15 7.39562L185.504 8.70073L194.649 8.26569L197.697 10.4409L200.31 11.3109L205.1 12.181L208.148 14.3562L210.325 16.0963L213.373 19.5766L213.809 23.927L216.857 24.7971L220.341 23.0569L222.518 19.1416L226.437 21.3168H231.227L232.969 16.9664L232.534 13.0511L242.984 8.26569L247.774 9.5708L252.129 11.746L262.58 9.5708L262.145 20.8818L256.484 31.3226L251.694 44.8088L246.033 57.4248V65.2555L242.114 74.3912L237.324 84.8321L232.969 90.0526L229.485 95.273L227.308 101.364L211.632 111.369L199.439 125.726L195.52 135.296L190.73 143.997L186.811 157.048L181.15 162.704L173.312 170.969L168.957 181.845L165.473 192.286L162.861 207.947L160.683 210.558L131.943 191.851L132.379 185.326L130.637 180.975V173.58L122.799 170.099L113.654 171.404L105.38 169.664L95.3649 169.229L83.6076 165.314L76.2048 162.269L69.673 163.574L69.2375 166.184L69.673 168.794L68.3666 169.229L65.3184 167.489L60.5284 159.658L55.3029 154.003L48.3356 150.088L46.1583 146.172L43.1101 142.692L44.852 135.296L40.4974 133.339L32.4415 130.728L28.7401 133.556L25.6919 126.596L25.2565 122.68L16.9828 117.025L13.0637 115.72L9.14458 117.895L0 114.415L6.9673 0Z";
            }

            @Override
            public Vector<Double, Double> getSVGLocation() {
                return new Vector<>(60.0, 258.0);
            }

            @Override
            public String getName() {
                return "NEW SOUtH WALES";
            }
        }, VIC{
            @Override
            public String getPath() {
                return "M6.9673 0L70.5439 5.22044L117.138 9.5708L169.392 15.2263L175.924 8.70073L181.15 7.39562L185.504 8.70073L194.649 8.26569L197.697 10.4409L200.31 11.3109L205.1 12.181L208.148 14.3562L210.325 16.0963L213.373 19.5766L213.809 23.927L216.857 24.7971L220.341 23.0569L222.518 19.1416L226.437 21.3168H231.227L232.969 16.9664L232.534 13.0511L242.984 8.26569L247.774 9.5708L252.129 11.746L262.58 9.5708L262.145 20.8818L256.484 31.3226L251.694 44.8088L246.033 57.4248V65.2555L242.114 74.3912L237.324 84.8321L232.969 90.0526L229.485 95.273L227.308 101.364L211.632 111.369L199.439 125.726L195.52 135.296L190.73 143.997L186.811 157.048L181.15 162.704L173.312 170.969L168.957 181.845L165.473 192.286L162.861 207.947L160.683 210.558L131.943 191.851L132.379 185.326L130.637 180.975V173.58L122.799 170.099L113.654 171.404L105.38 169.664L95.3649 169.229L83.6076 165.314L76.2048 162.269L69.673 163.574L69.2375 166.184L69.673 168.794L68.3666 169.229L65.3184 167.489L60.5284 159.658L55.3029 154.003L48.3356 150.088L46.1583 146.172L43.1101 142.692L44.852 135.296L40.4974 133.339L32.4415 130.728L28.7401 133.556L25.6919 126.596L25.2565 122.68L16.9828 117.025L13.0637 115.72L9.14458 117.895L0 114.415L6.9673 0Z";
            }

            @Override
            public Vector<Double, Double> getSVGLocation() {
                return new Vector<>(60.0, 258.0);
            }

            @Override
            public String getName() {
                return "VICTORIA";
            }
        }, TAS{
            @Override
            public String getPath() {
                return "M109.045 234.28L122.349 211.895L133.552 207.697L148.257 179.016H154.559L171.364 181.815L179.766 171.322L191.67 130.748L205.674 117.457L209.175 85.9781L210.576 59.3957L216.877 41.2077C216.877 41.2077 202.873 21.6207 200.072 21.6207C197.271 21.6207 179.766 33.5129 179.766 33.5129L143.355 34.2124L127.951 38.4096L102.743 44.7054L59.3299 23.7193L14.5164 0.634644L8.21453 1.33418L4.01326 16.0244L0.512207 30.7147L11.0154 63.5929L21.5185 88.0767L33.4221 110.462L38.3236 130.049L34.1223 143.34L33.4221 153.833L53.7282 198.603L64.2314 222.388L72.6339 226.585L88.0386 225.186L102.743 234.28H109.045Z";
            }

            @Override
            public Vector<Double, Double> getSVGLocation() {
                return new Vector<>(83.0, 295.0);
            }

            @Override
            public String getName() {
                return "TASMANIA";
            }
        };

        public abstract String getPath();
        public abstract Vector<Double, Double> getSVGLocation();
        public abstract String getName();
    }
}
