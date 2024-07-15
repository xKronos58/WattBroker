package com.wattbroker.wattbroker;

import com.util.util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
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
        final Boolean[] overlayAdded = {false};

        svg.setFill(interpolateColor(
                StartColour, endColour,
                normalise(price, minValue, maxValue)));

        // Set listener for state hover
        svg.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if(!overlayAdded[0] && !onOverlay[SA]) {
                map_root.getChildren().add(Overlays[SA]);
                overlayAdded[0] = true;
            }
        });
        svg.setOnMouseExited(e -> {
            if(onOverlay[SA])
                return;
            map_root.getChildren().removeIf(n -> n.getId() != null && n.getId().equals("icon_sa"));
            overlayAdded[0] = false;
        });
        svg.setOnMouseClicked(e -> {
            System.out.println("sa");
        });


        return svg;
    }

    private void addHoverItem(double price, util.Vector<Double, Double> pos, String state) {
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
    private util.Vector<Double, Double> getMinMax(double... values) {
        double min = values[0], max = values[0];
        for(double d : values) {
            if(d < min) min = d;
            else if(d > max) max = d; // Else as a value cant be min and max.
        }
        return new util.Vector<>(min, max);
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
}