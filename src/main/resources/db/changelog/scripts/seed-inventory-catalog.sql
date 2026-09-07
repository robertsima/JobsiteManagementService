--liquibase formatted sql

--changeset robertsima:seed-inventory-catalog-001
--comment: Expands each provider inventory into a realistic catalog so inventory search and
--comment: filtering have something to work against. Items 1-10 come from seed-data.sql and are
--comment: referenced by the seeded quote and order lines, so this file starts at id 11.
--comment: Facets are deliberately varied - category, brand, colour, material, unit of measure,
--comment: price band, stock level and the active flag - and a few rows are intentionally
--comment: out of stock or deactivated so those filters have negative cases to find.

-- ---------------------------------------------------------------------------
-- Inventory 1 - Home Depot Pro (provider 1), broad general construction catalog
-- ---------------------------------------------------------------------------
INSERT INTO public.items (id, inventory_id, sku, name, description, category, brand, unit_of_measure,
                          price, color, material, dimensions, weight, stock_quantity, active)
VALUES
    (11, 1, 'HD-2X6-10', '2x6x10 SPF Framing Lumber',
     'Kiln dried spruce-pine-fir dimensional framing lumber.', 'LUMBER', 'ProBuild', 'EACH',
     9.7200, 'Natural', 'Softwood', '1.5in x 5.5in x 10ft', '17.5 lb', 3600, TRUE),

    (12, 1, 'HD-2X8-12', '2x8x12 SPF Framing Lumber',
     'Kiln dried spruce-pine-fir joist and header stock.', 'LUMBER', 'ProBuild', 'EACH',
     16.4400, 'Natural', 'Softwood', '1.5in x 7.25in x 12ft', '27.8 lb', 1800, TRUE),

    (13, 1, 'HD-4X4-8', '4x4x8 Pressure Treated Post',
     'Ground contact rated treated post for decks and fences.', 'LUMBER', 'ProBuild', 'EACH',
     12.8700, 'Green', 'Treated Softwood', '3.5in x 3.5in x 8ft', '21.6 lb', 940, TRUE),

    (14, 1, 'HD-PLY-34', '3/4in Plywood Subfloor 4x8',
     'Tongue and groove rated subfloor panel.', 'SHEET_GOODS', 'ProBuild', 'EACH',
     54.2000, 'Natural', 'Plywood', '48in x 96in x 0.75in', '61.0 lb', 720, TRUE),

    (15, 1, 'HD-DRY-12', '1/2in Drywall Panel 4x8',
     'Standard gypsum wallboard for interior partitions.', 'DRYWALL', 'BoardRite', 'EACH',
     14.3500, 'White', 'Gypsum', '48in x 96in x 0.5in', '51.2 lb', 2400, TRUE),

    (16, 1, 'HD-DRY-58', '5/8in Type X Drywall 4x8',
     'Fire rated Type X gypsum board for rated assemblies.', 'DRYWALL', 'BoardRite', 'EACH',
     18.9000, 'White', 'Gypsum', '48in x 96in x 0.625in', '70.4 lb', 1500, TRUE),

    (17, 1, 'HD-SCR-2IN', '2in Drywall Screws, 5 lb Box',
     'Bugle head coarse thread drywall screws.', 'FASTENERS', 'GripFast', 'BOX',
     21.4500, 'Black', 'Coated Steel', '2in', '5 lb', 880, TRUE),

    (18, 1, 'HD-NAIL-16D', '16d Framing Nails, 50 lb Box',
     'Bright common nails for stick framing.', 'FASTENERS', 'GripFast', 'BOX',
     62.8000, 'Bright', 'Steel', '3.5in', '50 lb', 310, TRUE),

    (19, 1, 'HD-ANCH-38', '3/8in Wedge Anchors, 50 pack',
     'Galvanized wedge anchors for concrete fastening.', 'FASTENERS', 'IronGrip', 'BOX',
     44.1500, 'Zinc', 'Galvanized Steel', '3/8in x 3.75in', '6.4 lb', 260, TRUE),

    (20, 1, 'HD-CONC-80', '80 lb High Strength Concrete Mix',
     'High early strength 5000 psi concrete mix.', 'CONCRETE', 'SetRite', 'EACH',
     8.2400, 'Gray', 'Portland Cement', '80 lb bag', '80 lb', 2050, TRUE),

    (21, 1, 'HD-REBAR-4', '#4 Rebar 20ft',
     'Grade 60 deformed reinforcing bar.', 'CONCRETE', 'IronGrip', 'EACH',
     14.6000, 'Gray', 'Steel', '0.5in x 20ft', '13.4 lb', 1180, TRUE),

    (22, 1, 'HD-MESH-66', '6x6 Welded Wire Mesh Sheet',
     'Flat welded wire reinforcement for slabs on grade.', 'CONCRETE', 'IronGrip', 'EACH',
     11.9500, 'Gray', 'Steel', '5ft x 10ft', '22.0 lb', 640, TRUE),

    (23, 1, 'HD-PAINT-INT', 'Interior Latex Paint, 5 Gallon',
     'Low VOC interior latex, eggshell finish, tintable base.', 'PAINT', 'ColorCore', 'EACH',
     148.0000, 'White', 'Latex', '5 gal pail', '56.0 lb', 420, TRUE),

    (24, 1, 'HD-PAINT-EXT', 'Exterior Acrylic Paint, 5 Gallon',
     'Weather resistant exterior acrylic, satin finish.', 'PAINT', 'ColorCore', 'EACH',
     172.5000, 'White', 'Acrylic', '5 gal pail', '58.0 lb', 340, TRUE),

    (25, 1, 'HD-PRIMER-1', 'Multi Surface Primer, 1 Gallon',
     'Bonding primer for drywall, wood and masonry.', 'PAINT', 'ColorCore', 'GALLON',
     31.7500, 'White', 'Latex', '1 gal can', '11.2 lb', 610, TRUE),

    (26, 1, 'HD-ROMEX-12', '12/2 NM-B Building Wire, 250ft',
     'Non metallic sheathed cable with ground, 20 amp circuits.', 'ELECTRICAL', 'VoltLine', 'EACH',
     189.0000, 'Yellow', 'Copper', '250ft coil', '24.0 lb', 260, TRUE),

    -- discontinued line, still referenced by history but excluded from search
    (27, 1, 'HD-ROMEX-14', '14/2 NM-B Building Wire, 250ft',
     'Non metallic sheathed cable with ground, 15 amp circuits. Discontinued.',
     'ELECTRICAL', 'VoltLine', 'EACH',
     132.4000, 'White', 'Copper', '250ft coil', '17.5 lb', 380, FALSE),

    (28, 1, 'HD-BOX-1G', '1-Gang Old Work Box',
     'Remodel electrical box with wing brackets.', 'ELECTRICAL', 'VoltLine', 'EACH',
     2.1800, 'Blue', 'PVC', '3.75in x 2.25in x 2.5in', '0.3 lb', 4200, TRUE),

    (29, 1, 'HD-BRKR-20', '20A Single Pole Breaker',
     'Thermal magnetic single pole circuit breaker.', 'ELECTRICAL', 'VoltLine', 'EACH',
     8.9500, 'Black', 'Thermoplastic', '1in', '0.5 lb', 1450, TRUE),

    (30, 1, 'HD-INSUL-R13', 'R-13 Fiberglass Batt, 40 sqft',
     'Kraft faced batt insulation for 2x4 walls.', 'INSULATION', 'ThermaSeal', 'EACH',
     42.3000, 'Pink', 'Fiberglass', '15in x 32ft', '18.0 lb', 890, TRUE),

    (31, 1, 'HD-INSUL-R30', 'R-30 Fiberglass Batt, 31 sqft',
     'Kraft faced batt insulation for attics and floors.', 'INSULATION', 'ThermaSeal', 'EACH',
     51.8000, 'Pink', 'Fiberglass', '16in x 23ft', '22.5 lb', 640, TRUE),

    (32, 1, 'HD-HOUSE-WRAP', 'House Wrap, 9ft x 150ft',
     'Breathable weather resistive barrier roll.', 'INSULATION', 'ThermaSeal', 'EACH',
     178.0000, 'White', 'Polyolefin', '9ft x 150ft', '34.0 lb', 210, TRUE),

    (33, 1, 'HD-SHINGLE-AR', 'Architectural Shingles, Bundle',
     'Laminated architectural asphalt shingles, 3 bundles per square.',
     'ROOFING', 'TopShield', 'BUNDLE',
     38.6500, 'Charcoal', 'Asphalt', '33.3 sqft', '78.0 lb', 1620, TRUE),

    (34, 1, 'HD-FELT-30', '30 lb Roofing Felt Roll',
     'Asphalt saturated organic roofing underlayment.', 'ROOFING', 'TopShield', 'EACH',
     34.9000, 'Black', 'Asphalt Saturated Felt', '3ft x 72ft', '60.0 lb', 470, TRUE),

    -- backordered
    (35, 1, 'HD-ICE-SHIELD', 'Ice and Water Shield Roll',
     'Self adhering rubberized asphalt eave and valley membrane.',
     'ROOFING', 'TopShield', 'EACH',
     96.2500, 'Black', 'Rubberized Asphalt', '3ft x 65ft', '68.0 lb', 0, TRUE),

    (36, 1, 'HD-CIRC-SAW', '7-1/4in Circular Saw',
     'Corded 15 amp circular saw with electric brake.', 'TOOLS', 'IronGrip', 'EACH',
     129.0000, 'Blue', 'Composite', '14in x 10in x 9in', '8.8 lb', 140, TRUE),

    (37, 1, 'HD-DRILL-20V', '20V Cordless Hammer Drill Kit',
     'Brushless hammer drill with two batteries and charger.', 'TOOLS', 'IronGrip', 'EACH',
     199.0000, 'Blue', 'Composite', '12in x 10in x 4in', '6.2 lb', 95, TRUE),

    (38, 1, 'HD-LEVEL-48', '48in Box Beam Level',
     'Machined aluminum box beam level with three vials.', 'TOOLS', 'IronGrip', 'EACH',
     46.7500, 'Silver', 'Aluminum', '48in', '3.1 lb', 320, TRUE),

    (39, 1, 'HD-GLOVE-XL', 'Cut Resistant Work Gloves, XL',
     'ANSI A4 cut resistant nitrile coated gloves.', 'SAFETY', 'SafeGuard', 'EACH',
     14.2000, 'Gray', 'Nitrile Coated Knit', 'XL', '0.4 lb', 1900, TRUE),

    (40, 1, 'HD-CAULK-SIL', 'Silicone Construction Sealant',
     'Paintable silicone sealant for interior and exterior joints.',
     'ADHESIVES', 'BondTite', 'EACH',
     7.8500, 'Clear', 'Silicone', '10.1 oz tube', '0.8 lb', 2600, TRUE)
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------------
-- Inventory 2 - Ferguson Supply (provider 2), plumbing and mechanical
-- ---------------------------------------------------------------------------
INSERT INTO public.items (id, inventory_id, sku, name, description, category, brand, unit_of_measure,
                          price, color, material, dimensions, weight, stock_quantity, active)
VALUES
    (41, 2, 'FG-PVC-2IN', '2in PVC DWV Pipe',
     'Schedule 40 drain waste vent pipe.', 'PLUMBING', 'Charlotte', 'LINEAR_FOOT',
     8.6500, 'White', 'PVC', '2in diameter', '0.9 lb/ft', 3400, TRUE),

    (42, 2, 'FG-PVC-3IN', '3in PVC DWV Pipe',
     'Schedule 40 drain waste vent pipe.', 'PLUMBING', 'Charlotte', 'LINEAR_FOOT',
     13.2000, 'White', 'PVC', '3in diameter', '1.5 lb/ft', 2900, TRUE),

    (43, 2, 'FG-CPVC-050', '1/2in CPVC Water Pipe',
     'CTS CPVC hot and cold water distribution pipe.', 'PLUMBING', 'Charlotte', 'LINEAR_FOOT',
     2.4500, 'Tan', 'CPVC', '0.5in diameter', '0.14 lb/ft', 5600, TRUE),

    (44, 2, 'FG-PEX-050', '1/2in PEX-A Tubing, 100ft Coil',
     'Expansion PEX-A tubing for potable water.', 'PLUMBING', 'Uponor', 'EACH',
     68.9000, 'Red', 'PEX', '0.5in x 100ft', '5.2 lb', 740, TRUE),

    (45, 2, 'FG-PEX-075', '3/4in PEX-A Tubing, 100ft Coil',
     'Expansion PEX-A trunk line tubing.', 'PLUMBING', 'Uponor', 'EACH',
     112.3500, 'Blue', 'PEX', '0.75in x 100ft', '8.1 lb', 520, TRUE),

    (46, 2, 'FG-CU-050', '1/2in Type L Copper Tube',
     'Hard drawn copper water tube.', 'PLUMBING', 'Mueller', 'LINEAR_FOOT',
     4.8000, 'Copper', 'Copper', '0.5in diameter', '0.29 lb/ft', 6200, TRUE),

    (47, 2, 'FG-CU-100', '1in Type L Copper Tube',
     'Hard drawn copper water tube.', 'PLUMBING', 'Mueller', 'LINEAR_FOOT',
     11.7500, 'Copper', 'Copper', '1in diameter', '0.66 lb/ft', 2800, TRUE),

    (48, 2, 'FG-ELB-90-2', '2in PVC 90 Degree Elbow',
     'Schedule 40 DWV long sweep elbow.', 'PIPE_FITTINGS', 'Charlotte', 'EACH',
     3.4200, 'White', 'PVC', '2in', '0.3 lb', 4800, TRUE),

    (49, 2, 'FG-TEE-2IN', '2in PVC Sanitary Tee',
     'Schedule 40 DWV sanitary tee fitting.', 'PIPE_FITTINGS', 'Charlotte', 'EACH',
     5.6800, 'White', 'PVC', '2in', '0.5 lb', 3600, TRUE),

    (50, 2, 'FG-CPLG-CU-075', '3/4in Copper Coupling',
     'Wrot copper solder coupling with stop.', 'PIPE_FITTINGS', 'NIBCO', 'EACH',
     1.9500, 'Copper', 'Copper', '0.75in', '0.1 lb', 8400, TRUE),

    (51, 2, 'FG-BALL-075', '3/4in Brass Ball Valve',
     'Full port lead free brass ball valve.', 'VALVES', 'Apollo', 'EACH',
     16.4000, 'Brass', 'Brass', '0.75in NPT', '0.8 lb', 1240, TRUE),

    (52, 2, 'FG-BALL-2IN', '2in Brass Ball Valve',
     'Full port lead free brass ball valve.', 'VALVES', 'Apollo', 'EACH',
     68.9000, 'Brass', 'Brass', '2in NPT', '3.4 lb', 280, TRUE),

    -- superseded by the Apollo ball valves
    (53, 2, 'FG-GATE-1IN', '1in Bronze Gate Valve',
     'Rising stem bronze gate valve. Superseded by ball valve line.',
     'VALVES', 'NIBCO', 'EACH',
     42.1500, 'Bronze', 'Bronze', '1in NPT', '1.6 lb', 460, FALSE),

    (54, 2, 'FG-PRV-075', '3/4in Pressure Reducing Valve',
     'Adjustable water pressure reducing valve with strainer.', 'VALVES', 'Zurn', 'EACH',
     98.5000, 'Bronze', 'Bronze', '0.75in', '2.2 lb', 190, TRUE),

    (55, 2, 'FG-TOILET-EL', 'Elongated Two Piece Toilet',
     'Comfort height elongated toilet, 1.28 gpf.', 'FIXTURES', 'Kohler', 'EACH',
     218.0000, 'White', 'Vitreous China', '28in x 18in x 30in', '92.0 lb', 240, TRUE),

    (56, 2, 'FG-SINK-SS', 'Undermount Stainless Sink, 30in',
     '18 gauge single bowl undermount kitchen sink.', 'FIXTURES', 'Kohler', 'EACH',
     289.0000, 'Stainless', 'Stainless Steel', '30in x 18in x 9in', '32.0 lb', 160, TRUE),

    (57, 2, 'FG-FAUCET-KIT', 'Pull Down Kitchen Faucet',
     'Single handle pull down kitchen faucet with docking spray head.',
     'FIXTURES', 'Kohler', 'EACH',
     176.5000, 'Chrome', 'Brass', '16in height', '5.4 lb', 310, TRUE),

    (58, 2, 'FG-WH-50GAL', '50 Gallon Gas Water Heater',
     'Atmospheric vent natural gas water heater, 40k BTU.',
     'WATER_HEATERS', 'Rheem', 'EACH',
     742.0000, 'White', 'Glass Lined Steel', '22in diameter x 60in', '155.0 lb', 85, TRUE),

    -- long lead time, currently unstocked
    (59, 2, 'FG-WH-TANKLESS', 'Tankless Gas Water Heater, 199k BTU',
     'Condensing tankless natural gas water heater.',
     'WATER_HEATERS', 'Rheem', 'EACH',
     1284.0000, 'Silver', 'Stainless Steel', '26in x 18in x 11in', '82.0 lb', 0, TRUE),

    (60, 2, 'FG-DUCT-8IN', '8in Insulated Flex Duct, 25ft',
     'R-6 insulated flexible duct with vapour barrier.', 'HVAC', 'Honeywell', 'EACH',
     62.4000, 'Silver', 'Mylar', '8in x 25ft', '12.0 lb', 380, TRUE),

    (61, 2, 'FG-THERM-WIFI', 'Programmable WiFi Thermostat',
     'Seven day programmable thermostat with remote access.', 'HVAC', 'Honeywell', 'EACH',
     149.0000, 'White', 'Polycarbonate', '4.5in x 4.5in x 1in', '0.7 lb', 260, TRUE),

    (62, 2, 'FG-SOLV-CEM', 'PVC Solvent Cement, 32 oz',
     'Heavy bodied low VOC PVC solvent cement.', 'PLUMBING', 'Oatey', 'EACH',
     24.7500, 'Clear', 'Solvent', '32 oz can', '2.4 lb', 1450, TRUE)
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------------
-- Inventory 3 - 84 Lumber (provider 3), framing, engineered wood and decking
-- ---------------------------------------------------------------------------
INSERT INTO public.items (id, inventory_id, sku, name, description, category, brand, unit_of_measure,
                          price, color, material, dimensions, weight, stock_quantity, active)
VALUES
    (63, 3, 'EL-STUD-104', '2x4x104-5/8 Precut Stud',
     'Precut wall stud for 9ft plate height.', 'LUMBER', 'MillDirect', 'EACH',
     4.6200, 'Natural', 'Softwood', '1.5in x 3.5in x 104.625in', '9.9 lb', 8600, TRUE),

    (64, 3, 'EL-2X6-16', '2x6x16 #2 SYP',
     'Southern yellow pine dimensional lumber.', 'LUMBER', 'MillDirect', 'EACH',
     15.8800, 'Natural', 'Southern Yellow Pine', '1.5in x 5.5in x 16ft', '28.0 lb', 2400, TRUE),

    (65, 3, 'EL-2X12-16', '2x12x16 #2 SYP',
     'Southern yellow pine joist and stair stringer stock.',
     'LUMBER', 'MillDirect', 'EACH',
     42.3000, 'Natural', 'Southern Yellow Pine', '1.5in x 11.25in x 16ft', '56.0 lb', 720, TRUE),

    (66, 3, 'EL-6X6-12', '6x6x12 Pressure Treated Post',
     'Ground contact treated structural post.', 'LUMBER', 'MillDirect', 'EACH',
     58.7500, 'Green', 'Treated Softwood', '5.5in x 5.5in x 12ft', '74.0 lb', 340, TRUE),

    (67, 3, 'EL-OSB-58', '5/8in T and G OSB Subfloor 4x8',
     'Tongue and groove oriented strand board subfloor.',
     'SHEET_GOODS', 'MillDirect', 'EACH',
     28.4000, 'Natural', 'OSB', '48in x 96in x 0.625in', '58.0 lb', 1600, TRUE),

    (68, 3, 'EL-PLY-CDX-58', '5/8in CDX Plywood 4x8',
     'Exposure 1 rated CDX sheathing plywood.', 'SHEET_GOODS', 'MillDirect', 'EACH',
     41.2000, 'Natural', 'Plywood', '48in x 96in x 0.625in', '52.0 lb', 980, TRUE),

    (69, 3, 'EL-ZIP-716', '7/16in Structural Sheathing Panel',
     'Integrated water resistive barrier sheathing panel.',
     'SHEET_GOODS', 'TimberCore', 'EACH',
     33.9500, 'Green', 'OSB', '48in x 96in x 0.4375in', '47.0 lb', 1240, TRUE),

    (70, 3, 'EL-LVL-11875', '1-3/4in x 11-7/8in LVL Beam, 20ft',
     'Laminated veneer lumber header and beam stock.',
     'ENGINEERED_WOOD', 'TimberCore', 'EACH',
     186.0000, 'Natural', 'Laminated Veneer Lumber', '1.75in x 11.875in x 20ft', '96.0 lb', 210, TRUE),

    (71, 3, 'EL-IJOIST-117', '11-7/8in I-Joist, 20ft',
     'Engineered wood I-joist with OSB web.', 'ENGINEERED_WOOD', 'TimberCore', 'EACH',
     74.5000, 'Natural', 'Engineered Wood', '11.875in x 20ft', '42.0 lb', 660, TRUE),

    -- special order only
    (72, 3, 'EL-GLULAM-24', '5-1/8in x 24in Glulam Beam, 24ft',
     'Glue laminated structural timber beam, special order.',
     'ENGINEERED_WOOD', 'TimberCore', 'EACH',
     892.0000, 'Natural', 'Glue Laminated Timber', '5.125in x 24in x 24ft', '410.0 lb', 0, TRUE),

    (73, 3, 'EL-RIM-117', '11-7/8in Rim Board, 16ft',
     'Engineered rim board for I-joist floor systems.',
     'ENGINEERED_WOOD', 'TimberCore', 'EACH',
     58.2000, 'Natural', 'Engineered Wood', '11.875in x 16ft', '38.0 lb', 480, TRUE),

    (74, 3, 'EL-DECK-COMP', 'Composite Decking Board, 12ft',
     'Capped wood plastic composite grooved deck board.', 'DECKING', 'DeckPro', 'EACH',
     46.8000, 'Gray', 'Wood Plastic Composite', '0.94in x 5.5in x 12ft', '22.0 lb', 1400, TRUE),

    (75, 3, 'EL-DECK-PT', '5/4x6x12 Treated Deck Board',
     'Above ground treated radius edge deck board.', 'DECKING', 'DeckPro', 'EACH',
     14.2500, 'Green', 'Treated Softwood', '1in x 5.5in x 12ft', '15.0 lb', 2600, TRUE),

    (76, 3, 'EL-RAIL-KIT', 'Composite Railing Kit, 8ft',
     'Composite guard rail kit with balusters and brackets.', 'DECKING', 'DeckPro', 'EACH',
     168.0000, 'White', 'Composite', '8ft section', '34.0 lb', 220, TRUE),

    (77, 3, 'EL-TRIM-1X4', '1x4x16 Primed Finger Joint Trim',
     'Primed finger jointed pine casing and trim board.', 'TRIM', 'MillDirect', 'EACH',
     18.6000, 'White', 'Primed Pine', '0.75in x 3.5in x 16ft', '6.4 lb', 1100, TRUE),

    -- profile retired
    (78, 3, 'EL-CROWN-35', '3-5/8in Primed Crown Molding, 16ft',
     'Primed MDF crown molding. Profile retired.', 'TRIM', 'MillDirect', 'EACH',
     26.4000, 'White', 'Primed MDF', '3.625in x 16ft', '8.2 lb', 640, FALSE),

    (79, 3, 'EL-HANGER-210', '2x10 Joist Hanger',
     'Galvanized face mount joist hanger.', 'FRAMING_HARDWARE', 'FastenRight', 'EACH',
     2.8400, 'Zinc', 'Galvanized Steel', '2x10', '0.6 lb', 7200, TRUE),

    (80, 3, 'EL-STRAP-HUR', 'Hurricane Tie Strap',
     'Galvanized hurricane tie for rafter to plate connections.',
     'FRAMING_HARDWARE', 'FastenRight', 'EACH',
     1.3600, 'Zinc', 'Galvanized Steel', '1.5in x 8in', '0.2 lb', 9400, TRUE)
ON CONFLICT (id) DO NOTHING;

-- Keep the items sequence ahead of the explicit ids above
SELECT setval(pg_get_serial_sequence('public.items', 'id'), COALESCE((SELECT MAX(id) FROM public.items), 1));
