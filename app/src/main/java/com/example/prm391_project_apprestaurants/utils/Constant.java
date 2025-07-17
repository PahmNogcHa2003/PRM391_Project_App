package com.example.prm391_project_apprestaurants.utils;

public class Constant {
    public static final String SQL_CREATE_TABLE_USERS = """
            CREATE TABLE IF NOT EXISTS Users (
                Id INTEGER PRIMARY KEY AUTOINCREMENT,
                Username TEXT NOT NULL UNIQUE,
                Password TEXT NOT NULL,
                Email TEXT,
                Role TEXT DEFAULT 'User', -- 'User' hoặc 'Admin'
                CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                IsActive INTEGER DEFAULT 0
            );
            """;
    public static final String SQL_CREATE_TABLE_RESTAURANTS = """
            CREATE TABLE IF NOT EXISTS Restaurants (
                Id INTEGER PRIMARY KEY AUTOINCREMENT,
                Name TEXT NOT NULL,                    -- Tên quán ăn
                Description TEXT,                      -- Mô tả ngắn về quán
                Address TEXT NOT NULL,                 -- Địa chỉ đầy đủ
                District TEXT,                         -- Quận/Huyện (Hoàng Mai, Cầu Giấy,...)
                PriceRange TEXT,                       -- Mức giá: 'Dưới 30k', '30-50k',...
                Category TEXT,                         -- Loại món: Cơm, Bún, Mì, Lẩu,...
                OpeningHours TEXT,                     -- Giờ mở cửa, ví dụ: "8:00 - 22:00"
                PhoneNumber TEXT,                      -- Số điện thoại liên hệ
                Website TEXT,                          -- Website nếu có
                ImageUrl TEXT,                         -- URL ảnh đại diện
                Latitude REAL,                         -- Tọa độ GPS (nếu dùng Google Maps)
                Longitude REAL,                        -- Tọa độ GPS
                IsHidden INTEGER DEFAULT 0,            -- 0: hiển thị, 1: bị ẩn (Admin kiểm soát)
                MealTime TEXT DEFAULT 'all',           -- Giá trị: 'breakfast', 'lunch', 'dinner', 'all'
                CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                UpdatedAt TEXT DEFAULT CURRENT_TIMESTAMP
            );
            """;
    public static final String SQL_CREATE_TABLE_FAVORITES = """
            CREATE TABLE IF NOT EXISTS Favorites (
                Id INTEGER PRIMARY KEY AUTOINCREMENT,
                UserId INTEGER NOT NULL,
                RestaurantId INTEGER NOT NULL,
                CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (UserId) REFERENCES Users(Id),
                FOREIGN KEY (RestaurantId) REFERENCES Restaurants(Id)
            );
            """;
    public static final String SQL_CREATE_TABLE_REVIEWS = """
                CREATE TABLE IF NOT EXISTS Reviews (
                    Id INTEGER PRIMARY KEY AUTOINCREMENT,
                    UserId INTEGER NOT NULL,
                    RestaurantId INTEGER NOT NULL,
                    Content TEXT NOT NULL,
                    Rating INTEGER NOT NULL DEFAULT 5,
                    CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                    UpdatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (UserId) REFERENCES Users(Id),
                    FOREIGN KEY (RestaurantId) REFERENCES Restaurants(Id)
                );
            """;

    public static final String SQL_CREATE_TABLE_MENUS = """
                CREATE TABLE IF NOT EXISTS Menus (
                    Id INTEGER PRIMARY KEY AUTOINCREMENT,
                    RestaurantId INTEGER NOT NULL,
                    DishName TEXT NOT NULL,
                    Price TEXT NOT NULL,
                    ImageUrl TEXT,
                    Description TEXT,
                    CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                    IsHidden INTEGER DEFAULT 0,            -- 0: hiển thị, 1: bị ẩn (Admin kiểm soát)
                    FOREIGN KEY (RestaurantId) REFERENCES Restaurants(Id)
                );
            """;
    public static final String SQL_CREATE_TABLE_CATEGORIES = """
                CREATE TABLE IF NOT EXISTS Categories (
                    Id INTEGER PRIMARY KEY AUTOINCREMENT,
                    Name TEXT NOT NULL UNIQUE
                );
            """;
    public static final String SQL_CREATE_TABLE_RESTAURANT_CATEGORY = """
                CREATE TABLE IF NOT EXISTS RestaurantCategory (
                    RestaurantId INTEGER NOT NULL,
                    CategoryId INTEGER NOT NULL,
                    PRIMARY KEY (RestaurantId, CategoryId),
                    FOREIGN KEY (RestaurantId) REFERENCES Restaurants(Id),
                    FOREIGN KEY (CategoryId) REFERENCES Categories(Id)
                );
            """;


    public static final String SQL_CREATE_TABLE_REVIEW_MEDIA = """
                CREATE TABLE IF NOT EXISTS ReviewMedia (
                    Id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ReviewId INTEGER NOT NULL,
                    MediaUrl TEXT NOT NULL,
                    MediaType TEXT DEFAULT 'image', -- image | video
                    CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (ReviewId) REFERENCES Reviews(Id)
                );
            """;


    public static final String SQL_INSERT_TABLE_USERS = """ 
            INSERT INTO Users (Username, Password, Email, Role,IsActive) VALUES
            ('admin', 'adminpass', 'admin@gmail.com', 'Admin',1),
            ('user1', 'userpass1', 'user1@gmail.com', 'User',1),
            ('user2', 'userpass2', 'user2@gmail.com', 'User',1),
            ('user3', 'userpass3', 'user3@gmail.com', 'User',0),
            ('user4', 'userpass4', 'user4@gmail.com', 'User', 0),
            ('user5', 'userpass5', 'user5@gmail.com', 'User', 0);
            
            """;
    public static final String SQL_INSERT_TABLE_RESTAURANTS = """
            INSERT INTO Restaurants (Name, Description, Address, District, PriceRange, OpeningHours, PhoneNumber, Website, ImageUrl, Latitude, Longitude, MealTime) VALUES
            ('Phở Bát Đàn', 'Phở bò gia truyền với nước dùng đậm đà', '49 Bát Đàn', 'Hoàn Kiếm', '50-150k', '06:00 - 20:00', '0123456789', 'https://phobatdan.vn', 'https://media.mia.vn/uploads/blog-du-lich/Pho-bat-dan-pho-gia-truyen-100-nam-tuoi-tai-ha-noi-01-1639325605.jpg', 21.0331, 105.8489, 'breakfast'),
            ('Phở Thìn Lò Đúc', 'Phở bò nổi tiếng với nước dùng đặc biệt', '13 Lò Đúc', 'Hai Bà Trưng', '50-100k', '06:00 - 20:00', '0123456789', 'Không có', 'https://travelhanoi.org/wp-content/uploads/pho-thin-lo-duc-3.jpg', 21.0167, 105.8578, 'breakfast'),
            ('Phở 10 Lý Quốc Sư', 'Phở bò gia truyền, nước dùng đậm vị', '10 Lý Quốc Sư', 'Hoàn Kiếm', '50-150k', '06:00 - 22:00', '0123456789', 'https://guide.michelin.com/us/en/ha-noi/ha-noi_2974158/restaurant/pho-10-ly-quocsu', 'https://3.bp.blogspot.com/-7bothXsUu8I/WnUChsre6MI/AAAAAAAAFgQ/6FFfoWsF3m0Pcsejt36YZyEmbFs3p1CoACLcBGAs/s1600/2017-11-20%2B13.29.09.jpg', 21.0297, 105.8491, 'all'),
            ('Phở Gà Bà Nguyệt', 'Phở gà ta thơm ngon, nước dùng trong', '5 Phủ Doãn, P. Hàng Bông', 'Hoàn Kiếm', '30-50k', '06:00 - 14:00', '0123456789', 'https://guide.michelin.com/vn/en/ha-noi/ha-noi_2974158/restaurant/pho-ga-nguyet', 'https://axwwgrkdco.cloudimg.io/v7/__gmpics3__/6f8eab6f571b41ec87325f9d104e2703.jpeg?w=800&h=800&org_if_sml=1', 21.0319, 105.8472, 'breakfast'),
            ('Bún Chả Đắc Kim - Hàng Mành', 'Bún chả truyền thống Hà Nội, chả nướng thơm', '1 Hàng Mành', 'Hoàn Kiếm', '30-100k', '10:00 - 15:00', '0123456789', 'https://bunchahangmanh.com', 'https://bunchahangmanh.com/wp-content/uploads/2024/07/bun-cha-hang-manh-mon-ngon-dat-ha-thanh.jpg', 21.0301, 105.8492, 'lunch'),
            ('Bún riêu Huyền Thu', 'Bún riêu cua đậm đà, truyền thống', '2F Quang Trung, P. Trần Hưng Đạo', 'Hoàn Kiếm', '30-100k', '07:00 - 15:00', '0123456789', 'Không có', 'https://down-vn.img.susercontent.com/vn-11134259-7r98o-lw8yis72e2mx19@resize_ss576x330', 21.025000970851497, 105.84947511999545, 'lunch'),
            ('Nhà Hàng Nét Huế', 'Nếu là "fan cứng" của món bún bò Huế tại Quận Hoàn Kiếm, Hà Nội nhất định bạn phải ghé qua Nhà Hàng Nét Huế.', '198 Hàng Bông', 'Hoàn Kiếm', '100-200k', '07:00 - 22:00', '0912345678', 'https://nethue.com.vn', 'https://digiticket.vn/blog/wp-content/uploads/2021/05/net-hue-hang-bong-1.jpg', 21.02882481387945, 105.8440006707744, 'all'),
            ('Bún Ốc Bà Lương', 'Bún ốc Hà Nội chính gốc', '34 - 64 Ngõ 191 Khương Thượng', 'Đống Đa', '30-50k', '07:00 - 22:00', '0123456789', 'Không có', 'https://product.hstatic.net/1000275435/product/51223984_2601250186615598_5955656412913729536_o_e55356fd5498467b9d6d37a98abfaa4c_master.jpg', 21.0087, 105.8276, 'lunch'),
            ('Bánh Mì 25', 'Bánh mì pate và thịt nướng nổi tiếng', '25 Hàng Cá, P. Hàng Đào', 'Hoàn Kiếm', 'Dưới 30k', '07:00 - 21:00', '0123456789', 'Không có', 'https://tse3.mm.bing.net/th/id/OIP.xCjqHDEhV7Qs02p3EnGicQHaJO?rs=1&pid=ImgDetMain&o=7&rm=3', 21.0347, 105.8498, 'all'),
            ('Bánh Mì Bà Dần', 'Bánh mì pate truyền thống, giòn rụm', '34 Lò Sũ', 'Hoàn Kiếm', 'Dưới 30k', '06:00 - 14:00', '0123456789', 'Không có', 'https://tse1.mm.bing.net/th/id/OIP.LjWRIZ9bDKw32b90Z3fuZwHaHa?rs=1&pid=ImgDetMain&o=7&rm=3', 21.0333, 105.8532, 'breakfast'),
            ('Bánh Mì P', 'Bánh mì pate, thịt nướng đa dạng', '12 Hàng Bông', 'Hoàn Kiếm', 'Dưới 30k', '07:00 - 20:00', '0192370312', 'Không có', 'https://icdn.dantri.com.vn/thumb_w/960/2017/photo-6-1501035065366.jpg', 21.0297, 105.8478, 'all'),
            ('Bánh Mì Phố', 'Bánh mì trứng pate, giòn thơm', '45 Hàng Điếu', 'Hoàn Kiếm', 'Dưới 30k', '06:00 - 14:00', '0906729103', 'Không có', 'https://ticotravel.com.vn/wp-content/uploads/2022/05/Banh-mi-Ha-Noi-5.jpg', 21.0323, 105.8487, 'breakfast'),
            ('Cơm Tấm Sài Gòn', 'Cơm tấm chuẩn vị miền Nam', '12 Tôn Đức Thắng', 'Đống Đa', '30-50k', '07:00 - 22:00', '0901234567', 'Không có', 'https://scontent.fhan14-2.fna.fbcdn.net/v/t39.30808-6/498550038_1005804925076592_235301906947866347_n.jpg?_nc_cat=108&ccb=1-7&_nc_sid=833d8c&_nc_eui2=AeHxTShBMuVUNzV0jJxjXvsx8Y4gQGqGvejxjiBAaoa96O5AfC6iI2YJTLQP6L7LJ_kQHkR2KCg7RrYeMmmN7omk&_nc_ohc=2Ec__MBdPYoQ7kNvwEEcFrB&_nc_oc=AdkDxh1oXWkqig_PLi6m1dG6PbEwYByapsZnw5RBS1A3xu6cpzVrjTGUZ2dqX-QU0OH2HgUceZ0DsvT05Xzqw8bz&_nc_zt=23&_nc_ht=scontent.fhan14-2.fna&_nc_gid=VAg6pdBJrxo8hyt5hmnN5A&oh=00_AfSvs2qd1RgnK4ZXKcAPcG95Jc4Dvh9g4d9c-9rXk0GXOw&oe=687AF80B', 21.0245, 105.8291, 'lunch'),
            ('Cơm Gà Hải Nam', 'Cơm gà Hải Nam mềm thơm, chuẩn vị', '67 Lê Văn Hưu', 'Hai Bà Trưng', '50-100k', '10:00 - 21:00', '0912010132', 'Không có', 'https://dynamic-media-cdn.tripadvisor.com/media/photo-o/26/d0/f9/76/caption.jpg?w=900&h=500&s=1', 21.0192, 105.8561, 'lunch'),
            ('Cơm Niêu Hải Sư Singapore', 'quán cơm niêu Hà Nội ăn là nhớ', 'số 11 K1 Giảng Võ', 'Đống Đa', '50-100k', '10:00 - 21:00', '0987121132', 'Không có', 'https://lh3.googleusercontent.com/gps-cs-s/AC9h4nprrrXv_UIGfH1r-cx552fZ3f40EKjJeFwuftqyKhUF_wc4ww-UxZyhn8a-_BKH2zlDkaunoJpsUb4HyxW4HNUAXQRSVVzaHeGUo-fP45pUkgscd2voFj5ts5Ze9dqGmkdoW-17=w408-h306-k-no', 21.0156, 105.8567, 'lunch'),
            ('Cơm Rang Đức Hạnh', 'Cơm rang dưa bò đặc biệt, thơm ngon', 'số 38 phường Mã Mây', 'Hoàn Kiếm', '30-50k', '10:00 - 21:00', '0903435113', 'Không có', 'https://lalago.vn/wp-content/uploads/2025/03/com-rang-dua-bo-ha-noi-7.jpg', 21.0131, 105.8202, 'lunch'),
            ('Uu Dam Chay', 'Ẩm thực chay tinh tế, đa dạng món', '34 Nguyễn Du', 'Hoàn Kiếm', '50-100k', '10:00 - 21:00', '00', 'https://www.tripadvisor.com.vn/Restaurant_Review-g293924-d25567161-Reviews-Uu_Dam_Chay_Hanoi-Hanoi.html', 'https://dynamic-media-cdn.tripadvisor.com/media/photo-o/2d/c9/90/39/caption.jpg?w=900&h=500&s=1', 21.0256, 105.8512, 'lunch'),
            ('Chay An Lạc', 'Ẩm thực chay thanh tịnh, phong phú', '109 Trần Hưng Đạo', 'Hoàn Kiếm', '30-50k', '10:00 - 20:00', '0902997115', 'Không có', 'https://tadiha.com.vn/uploads/9919/2.-bufet-chay-an-lac-quan-767x1024-large.webp', 21.0089, 105.8312, 'lunch'),
            ('Chay Bồ Đề', 'Ẩm thực chay phong phú, thanh tịnh', '65 Quán Sứ', 'Hoàn Kiếm', '50k-100k', '10:00 - 20:00', '0906331527', 'Không có', 'https://amthucdochay.com/wp-content/uploads/2021/05/Nha-hang-chay-Ha-Noi-phia-ben-ngoai-nha-hang-Bo-De-chay-1.jpg', 21.0578, 105.8674, 'lunch'),
            ('Chay Hoa Sen', 'Ẩm thực chay cao cấp, tinh tế', '56 Nguyễn Khuyến', 'Đống Đa', '50-100k', '10:00 - 21:00', '0993221365', 'Không có', 'https://cdn.xanhsm.com/2025/02/fe40af2e-quan-chay-dong-da-1.jpg', 21.0267, 105.8378, 'lunch'),
            ('Nướng Nhà Gỗ', 'Nướng BBQ với không gian ấm cúng', '89 Láng Hạ', 'Đống Đa', '150-250k', '16:00 - 23:00', '0983232110', 'Không có', 'https://hotel84.com/hotel84-images/news/img1/tiem-nuong-nha-go.jpg', 21.0162, 105.8134, 'dinner'),
            ('Nướng Lão Ngư', 'Nướng BBQ kiểu Việt, thịt tươi ngon', '56 Thái Hà', 'Đống Đa', '150-250k', '16:00 - 23:00', '0983222111', 'Không có', 'https://hotel84.com/userfiles/image/nhahang/hanoi/LaoNgu/nha-hang-cha-ca-lao-ngu.jpg', 21.0145, 105.8213, 'dinner'),
            ('Nướng Hẻm', 'Nướng đường phố phong cách Hà Nội', '45 Ngõ 76 Chùa Láng', 'Đống Đa', '100-180k', '16:00 - 23:00', '0983000111', 'Không có', 'https://tse1.mm.bing.net/th/id/OIP.WE-SFmHFcu1utRi0H-3mAAHaE7?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 21.0213, 105.8112, 'dinner'),
            ('Nướng Ông Mười', 'Nướng BBQ hải sản tươi ngon', '78 Phạm Ngọc Thạch', 'Đống Đa', '180-250k', '16:00 - 23:00', '0983111222', 'Không có', 'https://giadinh.mediacdn.vn/296230595582509056/2023/5/16/34627618963246696376213828641660828420257568n-16842253456071277798188.jpeg', 21.0078, 105.8345, 'dinner'),
            ('Lẩu Thái Hải Sản', 'Lẩu Thái chua cay, hải sản tươi', '123 Trần Duy Hưng', 'Cầu Giấy', '150-250k', '11:00 - 22:00', '0983111333', 'Không có', 'https://cdn.nguyenkimmall.com/images/companies/_1/tin-tuc/kinh-nghiem-meo-hay/n%E1%BA%A5u%20%C4%83n/nau-lau-thai-chuan-vi-ngon-nhu-the-nao.jpg', 21.0076, 105.7943, 'dinner'),
            ('Lẩu Phố', 'Lẩu hải sản và lẩu thập cẩm đa dạng', '78 Giải Phóng', 'Hai Bà Trưng', '120-220k', '11:00 - 22:00', '0983111444', 'Không có', 'https://hotel84.com/hotel84-images/news/photo/lau-pho-o-hue.jpg', 21.0021, 105.8498, 'dinner'),
            ('Lẩu Nấm Ashima', 'Lẩu nấm cao cấp, nguyên liệu tươi', '23 Nguyễn Khang', 'Cầu Giấy', '200-300k', '11:00 - 22:00', '0983111555', 'Không có', 'https://th.bing.com/th/id/R.49fbbc6b22c306d9fa6cd9a38d4fe150?rik=rD5A5q9%2bkEJRDg&pid=ImgRaw&r=0', 21.0156, 105.7945, 'dinner'),
            ('Lẩu Bò Nhúng Dấm', 'Lẩu bò nhúng dấm đặc biệt, đậm đà', '45 Nguyễn Văn Cừ', 'Long Biên', '120-200k', '11:00 - 22:00', '0983111666', 'Không có', 'https://alltop.vn/backend/media/images/posts/1284/Bo_ngon_555-100635.jpg', 21.0432, 105.8654, 'dinner'),
            ('Xôi Yến', 'Xôi gà và xôi thập cẩm nổi tiếng', '35B Nguyễn Hữu Huân', 'Hoàn Kiếm', '20-35k', '06:00 - 13:00', '02439341956', 'Không có', 'https://truongdaihocvietnam.com/wp-content/uploads/2023/10/xoi-yen1.jpg', 21.0335, 105.8539, 'breakfast'),
            ('Xôi Gà Bà Hồi', 'Xôi gà thơm ngon, truyền thống', '45 Ngõ 165 Thái Hà', 'Đống Đa', '20-35k', '06:00 - 14:00', '0983111777', 'Không có', 'https://toplist.vn/images/800px/xoi-ga-ba-chieu-936325.jpg', 21.0132, 105.8201, 'breakfast'),
            ('Xôi Xéo Bà Hạt', 'Xôi xéo truyền thống, thơm ngon', '12 Đinh Liệt', 'Hoàn Kiếm', '20-35k', '06:00 - 12:00', '0983111888', 'Không có', 'https://1phutsaigon.vn/wp-content/uploads/2023/07/xoi-xeo-ba-3-beo-2.jpg', 21.0321, 105.8523, 'breakfast'),
            ('Xôi Lạc', 'Xôi thập cẩm truyền thống, phong phú', '23 Ngõ 45 Hào Nam', 'Đống Đa', '20-35k', '06:00 - 14:00', '0983111999', 'Không có', 'https://th.bing.com/th/id/R.0214c5c679f0ca96669748f9d2a31473?rik=uZQPIiJffbnlPw&pid=ImgRaw&r=0', 21.0265, 105.8367, 'breakfast'),
            ('Mì Vằn Thắn Hải Nam', 'Mì vằn thắn Hong Kong', '26 Hàng Bồ', 'Hoàn Kiếm', '35-50k', '07:00 - 22:00', '0983111223', 'Không có', 'https://toplist.vn/images/800px/dac-loi-quan-an-trung-hoa-1114806.jpg', 21.0345, 105.8510, 'all'),
            ('Mì Trộn Indomie', 'Mì trộn kiểu Hàn Quốc', '15 Tạ Quang Bửu', 'Hai Bà Trưng', '25-40k', '09:00 - 22:00', '0987123987', 'Không có', 'https://tse2.mm.bing.net/th/id/OIP.fdl7nFERw1Pj025ALIgdGQHaEo?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 21.0056, 105.8570, 'all'),
            ('Mì Ý Pasta Box', 'Mì Ý hiện đại', '12 Nguyễn Khánh Toàn', 'Cầu Giấy', '40-60k', '10:00 - 22:00', '02437955555', 'Không có', 'https://tse1.explicit.bing.net/th/id/OIP.E2erwF4Iawyoi3Qr9ufinwHaFz?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 21.0367, 105.7950, 'lunch,dinner'),
            ('Mì Quảng Bà Mành', 'Mì Quảng đặc sản', '2 Hàng Mành', 'Hoàn Kiếm', '35-50k', '08:00 - 20:00', '0983111445', 'Không có', 'https://hotel84.com/userfiles/image/nhahang/danang/myquangbamua/my-quang-ba-mua.jpg', 21.0357, 105.8500, 'lunch,dinner'),
            ('Trà Sữa Nhà Làm', 'Trà sữa homemade với hương vị tự nhiên', '45 Hàng Bông', 'Hoàn Kiếm', '20-35k', '09:00 - 22:00', '0983111556', 'Không có', 'https://th.bing.com/th/id/R.71176670378cf9c1c58538e24f86d77a?rik=5KcI5QVtoOwOgg&pid=ImgRaw&r=0', 21.0298, 105.8476, 'all'),
            ('Nước Mía Tạ Hiện', 'Nước mía tươi nguyên chất', '12 Tạ Hiện', 'Hoàn Kiếm', '15-25k', '09:00 - 22:00', '0983111667', 'Không có', 'https://artcoffee.vn/wp-content/uploads/quan-tra-sua-tra-dao-via-he.jpg', 21.0341, 105.8526, 'all'),
            ('Nước Ép Trái Cây Tươi', 'Nước ép trái cây nguyên chất, tươi ngon', '67 Hàng Bạc', 'Hoàn Kiếm', '20-35k', '09:00 - 22:00', '0983111778', 'Không có', 'https://data-service.pharmacity.io/pmc-upload-media/production/pmc-ecm-asm/posts/uong-nuoc-ep-1.webp', 21.0339, 105.8514, 'all'),
            ('Sinh Tố Cô Ba', 'Sinh tố và nước ép đa dạng, tươi mát', '12 Hàng Gai', 'Hoàn Kiếm', '20-35k', '09:00 - 22:00', '0983111889', 'Không có', 'https://mimihan.tw/wp-content/uploads/20190528150656_48.jpg', 21.0309, 105.8501, 'all');
            """;


    public static final String SQL_INSERT_TABLE_FAVORITES = """ 
            
            INSERT INTO Favorites (UserId, RestaurantId) VALUES
            (2, 1),
            (2, 2),
            (2, 3),
            (3, 3),
            (5, 4),
            (5, 5);
            """;
    public static final String SQL_INSERT_TABLE_REVIEWS = """
                           INSERT INTO Reviews (UserId, RestaurantId, Content, Rating) VALUES
            -- Phở Bát Đàn (RestaurantId = 1)
            (1, 1, 'Phở rất ngon, nước dùng đậm đà, thịt bò mềm.', 5),
            (2, 1, 'Hơi đông khách nhưng rất đáng để thử!', 4),
            (3, 1, 'Mình đến sáng sớm và vẫn phải xếp hàng.', 4),
            (4, 1, 'Không gian nhỏ nhưng phở chuẩn vị Hà Nội.', 5),
            
            -- Phở Thìn Lò Đúc (RestaurantId = 2)
            (1, 2, 'Phở béo ngậy, nước dùng đậm đặc biệt.', 5),
            (2, 2, 'Thịt bò áp chảo siêu ngon.', 5),
            (3, 2, 'Phục vụ nhanh, giá hợp lý.', 4),
            
            -- Phở 10 Lý Quốc Sư (RestaurantId = 3)
            (1, 3, 'Phở rất chất lượng, không gian sạch sẽ.', 4),
            (2, 3, 'Thịt tái ngon, nước dùng đậm.', 5),
            (3, 3, 'Giá hơi cao nhưng đáng tiền.', 4),
            (4, 3, 'Phở chuẩn vị truyền thống, nhân viên thân thiện.', 5),
            
            -- Phở Gà Bà Nguyệt (RestaurantId = 4)
            (1, 4, 'Phở gà ta ngon, nước dùng trong vắt.', 5),
            (2, 4, 'Gà dai ngon, hành lá nhiều hơi nồng.', 4),
            (3, 4, 'Không gian sạch, phục vụ tốt.', 4),
            
            -- Bún Chả Đắc Kim (RestaurantId = 5)
            (1, 5, 'Chả nướng thơm, nước chấm vừa miệng.', 5),
            (2, 5, 'Bún chả truyền thống đúng chất Hà Nội.', 5),
            (3, 5, 'Giá hơi cao nhưng chất lượng.', 4),
            (4, 5, 'Không gian chật nhưng đồ ăn tuyệt vời.', 4);
            """;


    public static final String SQL_INSERT_TABLE_MENUS = """
                       INSERT INTO Menus (RestaurantId, DishName, Price, ImageUrl, Description) VALUES
            (1, 'Phở bò tái', 60000, 'https://tse1.mm.bing.net/th/id/OIP.78Bd3g4uzrWD6hZlhnzgKAHaE8?rs=1&pid=ImgDetMain&o=7&rm=3', 'Thịt bò tái mềm, nước dùng đậm vị'),
            (1, 'Phở bò chín', 65000, 'https://th.bing.com/th/id/R.a71fa186dc6b8dd4b7264574442d2e52?rik=u7RRBrECScYLPA&pid=ImgRaw&r=0', 'Thịt bò chín kỹ, nước phở ngọt thanh'),
            (1, 'Phở gầu', 70000, 'https://tse1.explicit.bing.net/th/id/OIP.Xer4G9EAcQjT84hDGwvSAwHaFf?rs=1&pid=ImgDetMain&o=7&rm=3', 'Gầu bò béo ngậy, mềm tan'),
            (1, 'Phở bò nạm', 70000, 'https://th.bing.com/th/id/R.0a750a013debbe37bc68392d85ea9aca?rik=Mc2iKVDmOeLkGg&pid=ImgRaw&r=0', 'Thịt nạm mềm, ăn kèm rau thơm'),
            (1, 'Phở tái nạm gầu', 75000, 'https://tse2.mm.bing.net/th/id/OIP.LqJVUfX0vesd8caspXJkkgHaGG?rs=1&pid=ImgDetMain&o=7&rm=3', 'Tổng hợp 3 loại thịt hấp dẫn trong một bát'),
            (2, 'Phở bò tái lăn', 70000, 'https://th.bing.com/th/id/R.b45812d326d13741893e5a7c2de8f790?rik=3H4ITBULNosCvA&pid=ImgRaw&r=0', 'Thịt bò được xào nhanh trước khi chan nước dùng'),
            (2, 'Phở bò chín', 65000, 'https://th.bing.com/th/id/R.ae82de002827a31e2f6dce58a0e04782?rik=k1aitde0u7DzeA&pid=ImgRaw&r=0', 'Thịt bò nấu chín, nước dùng béo ngậy'),
            (2, 'Phở nạm gầu', 75000, 'https://tse1.mm.bing.net/th/id/OIP.FaEh5990458ECoEzngF99wHaHa?pid=ImgDet&w=474&h=474&rs=1&o=7&rm=3', 'Nạm gầu béo mềm hấp dẫn'),
            (2, 'Phở bò đặc biệt', 80000, 'https://halotravel.vn/wp-content/uploads/2020/10/pho-bat-dan3-e1603927789230.jpg', 'Tổng hợp các loại thịt trong một tô phở đặc biệt'),
            (2, 'Trà đá', 5000, 'https://static-images.vnncdn.net/files/publish/2022/10/17/name-159-1304.jpg', 'Giải khát mát lạnh ăn kèm phở'),
            (3, 'Phở bò tái lăn', 75000, 'https://pho10lyquocsu.com.vn/watermark/product/540x540x1/upload/product/z2343703734202511313b61907e2f570369ba5f32ead04-6355.jpg', 'Thịt bò tái mềm, hương vị đặc trưng'),
            (3, 'Phở nạm', 70000, 'https://pho10lyquocsu.com.vn/watermark/product/540x540x1/upload/product/z23437012152839390f55b1d1f6b82b6d41b04993146ed-6173.jpg', 'Thịt nạm béo thơm mềm mại'),
            (3, 'Phở tái gầu', 75000, 'https://pho10lyquocsu.com.vn/watermark/product/540x540x1/upload/product/9d3ded1971938fcdd682-7895.jpg', 'Kết hợp thịt tái và gầu ngon miệng'),
            (3, 'Phở đặc biệt', 85000, 'https://pho10lyquocsu.com.vn/watermark/product/540x540x1/upload/product/anh-chup-man-hinh-2023-07-11-luc-124746-8268.png', 'Bát phở đầy đặn gồm nhiều loại thịt'),
            (3, 'Quẩy nóng', 10000, 'https://pho10lyquocsu.com.vn/watermark/product/540x540x1/upload/product/1441352597-cach-lam-quay-14-6954.jpg', 'Ăn kèm phở nóng hổi'),
            (4, 'Phở gà ta', 50000, 'https://axwwgrkdco.cloudimg.io/v7/__gmpics__/6f8eab6f571b41ec87325f9d104e2703', 'Thịt gà ta thơm, nước trong'),
            (4, 'Phở gà xé', 55000, 'https://img-global.cpcdn.com/recipes/b711dcb7a259c93c/680x482cq70/ph%E1%BB%9F-ga-xe-d%C6%A1n-gi%E1%BA%A3n-recipe-main-photo.jpg', 'Gà xé sợi đều tay, thơm mềm'),
            (4, 'Phở gà đùi', 60000, 'https://tse1.mm.bing.net/th/id/OIP.2bnMBYjGZWzROevhXmhpVQHaEF?rs=1&pid=ImgDetMain&o=7&rm=3', 'Đùi gà ta nguyên miếng'),
            (4, 'Phở lòng gà', 60000, 'https://tripzone.vn/uploads/202103/09/07/091851-instagram--troosismeee.jpg', 'Phở ăn cùng lòng gà béo ngậy'),
            (4, 'Trứng non', 15000, 'https://img-global.cpcdn.com/recipes/41d6e29a7f6cdbb6/680x482cq70/ph%E1%BB%9F-ga-tr%E1%BB%A9ng-non-recipe-main-photo.jpg', 'Ăn kèm thêm cho khách thích lòng gà'),
            (5, 'Bún chả thịt nướng', 60000, 'https://axwwgrkdco.cloudimg.io/v7/__gmpics__/36c6a979a935439ab6e29102522b327b?width=1000', 'Chả nướng thơm lừng, ăn kèm bún và rau sống'),
            (5, 'Bún chả nem cua bể', 75000, 'https://toplist.vn/images/800px/bun-cha-nem-cua-be-913564.jpg
            ', 'Chả nướng và nem cua bể giòn tan'),
            (5, 'Nem cua bể riêng', 40000, 'https://bizweb.dktcdn.net/thumb/1024x1024/100/442/413/products/bun-nem-cua-be-01.png?v=1657640369343', 'Một suất nem cua bể thêm cho khách'),
            (5, 'Chả miếng', 30000, 'https://bizweb.dktcdn.net/thumb/1024x1024/100/442/413/products/bun-cha-mieng-02.png?v=1657639789840', 'Chả thịt lát miếng, thơm mềm'),
            (5, 'Trà đá', 5000, 'https://static-images.vnncdn.net/files/publish/2022/10/17/name-159-1304.jpg', 'Giải khát mát lạnh'),
            (6, 'Bún riêu cua', 40000, 'https://tse4.mm.bing.net/th/id/OIP.SJZy8UXQwD1NbTAMr06x0AHaJQ?w=660&h=825&rs=1&pid=ImgDetMain&o=7&rm=3', 'Riêu cua truyền thống, đậm đà hương vị Bắc'),
            (6, 'Bún riêu mọc', 45000, 'https://static.vinwonders.com/production/bun-rieu-ngon-ha-noi-6.jpg', 'Kết hợp mọc và riêu cua'),
            (6, 'Bún riêu bò', 50000, 'https://luhanhvietnam.com.vn/du-lich/vnt_upload/news/11_2022/hang-bun-rieu-lau-doi-o-ha-noi-huyen-thu-_.jpg', 'Bò và riêu cua ăn cùng bún tươi'),
            (6, 'Đậu rán', 10000, 'https://th.bing.com/th/id/R.bb5c427876370224f0a32dcdc9735c0a?rik=9WusGEL408clxg&pid=ImgRaw&r=0', 'Đậu rán giòn ăn kèm bún'),
            (6, 'Trà đá', 5000, 'https://static-images.vnncdn.net/files/publish/2022/10/17/name-159-1304.jpg', 'Giải khát kèm món bún nóng'),
            (7, 'Bún bò Huế đặc biệt', 85000, 'https://nethue.com.vn/temp/-uploaded-san%20pham_bun%20bo%20dac%20biet_cr_521x521.jpg', 'Đậm đà vị Huế, đầy đủ giò, chả, thịt'),
            (7, 'Bún bò giò heo', 90000, 'https://nethue.com.vn/temp/-uploaded-san%20pham_bun%20so%202_cr_521x521.jpg', 'Giò heo mềm, béo ăn cùng bún nóng'),
            (7, 'Bánh bèo chén', 45000, 'https://nethue.com.vn/temp/-uploaded-san%20pham_banh%20beo%20thit%20bam_cr_521x521.jpg', 'Bánh bèo mềm, mịn, nhân đậm đà'),
            (7, 'Bánh nậm', 40000, 'https://nethue.com.vn/temp/-uploaded-_thumbs-Hue%20cake_nam%20cake_cr_521x521.png', 'Món bánh Huế nổi tiếng, thơm lừng'),
            (7, 'Chè sen Huế', 35000, 'https://nethue.com.vn/temp/-uploaded-mon%20trang%20mieng_che%20sen_cr_521x521.png', 'Hương sen Huế dịu nhẹ, thanh mát'),
            (8, 'Bún ốc nguội', 30000, 'https://culaogieng.com/wp-content/uploads/2023/05/cach-lam-bun-oc-nguoi-4.jpg', 'Món bún độc đáo, nước dùng mát lạnh'),
            (8, 'Bún ốc nóng', 35000, 'https://ttol.vietnamnetjsc.vn/images/2022/12/14/15/26/bun-oc-4.jpg', 'Ốc giòn, nước dùng cay nồng'),
            (8, 'Bún ốc chuối đậu', 45000, 'https://th.bing.com/th/id/R.40dc9c001e7ef32872e4456764486573?rik=8ar5JW%2bW5FZyfQ&pid=ImgRaw&r=0', 'Bún ăn kèm chuối xanh, đậu phụ, riêu'),
            (8, 'Bún ốc thập cẩm', 50000, 'https://hotel84.com/userfiles/image/nhahang/hanoi/2bunCha/coLan/bun-oc-thap-cam-khuong-thuo.jpg', 'Gồm ốc, thịt, đậu, chuối, giò...'),
            (8, 'Trà chanh', 10000, 'https://tse1.mm.bing.net/th/id/OIP.oQyy3IPaz9NkZ-Zfy4PfjQHaHa?rs=1&pid=ImgDetMain&o=7&rm=3', 'Giải nhiệt ăn kèm món ốc nóng'),
            (9, 'Bánh mì pate thịt nguội', 25000, 'https://th.bing.com/th/id/R.52b7bd34393138626a56948b6adb8dd6?rik=AQx4eKPCv4qKRw&pid=ImgRaw&r=0', 'Thịt nguội, pate thơm béo'),
            (9, 'Bánh mì thịt nướng', 30000, 'https://tse1.mm.bing.net/th/id/OIP.v_ZQwj2PCy5dwBgldOw_-QHaEK?rs=1&pid=ImgDetMain&o=7&rm=3', 'Thịt nướng than hoa thơm phức'),
            (9, 'Bánh mì trứng', 20000, 'https://banhmimahai.vn/wp-content/uploads/2021/12/fbsr-1024x1024.jpg', 'Đơn giản mà hấp dẫn'),
            (9, 'Bánh mì chay', 20000, 'https://yummyday.vn/uploads/images/banh-mi-chay.jpg', 'Dành cho người ăn chay'),
            (9, 'Sữa đậu nành', 10000, 'https://down-vn.img.susercontent.com/vn-11134259-7r98o-lwg10luohe5705', 'Thức uống đi kèm bánh mì'),
            (10, 'Bánh mì pate truyền thống', 25000, 'https://down-vn.img.susercontent.com/vn-11134259-7ras8-m2q5yusg9b5m6f', 'Pate gan heo béo ngậy, giòn rụm'),
            (10, 'Bánh mì trứng ốp la', 20000, 'https://tse2.mm.bing.net/th/id/OIP.U8apHjB34usLc5yy_UuHOgHaD4?rs=1&pid=ImgDetMain&o=7&rm=3', 'Trứng chiên lòng đào hấp dẫn'),
            (10, 'Bánh mì thịt nguội', 25000, 'https://th.bing.com/th/id/R.52b7bd34393138626a56948b6adb8dd6?rik=AQx4eKPCv4qKRw&pid=ImgRaw&r=0', 'Thịt nguội ăn kèm pate thơm béo'),
            (10, 'Bánh mì chả lụa', 20000, 'https://tse3.mm.bing.net/th/id/OIP.pVbBPqh9ZTSLmsvs4MvwSwHaD5?rs=1&pid=ImgDetMain&o=7&rm=3', 'Chả lụa thơm ngon, hợp khẩu vị'),
            (10, 'Bánh mì pate trứng', 25000, 'https://patecotden.net/wp-content/uploads/2023/02/banh-mi-que-pate-cot-den-2-1.jpg', 'Kết hợp trứng và pate, béo mà không ngán'),
            
            
            
            
            -- RestaurantId 11: Bánh Mì P (Category: Bánh mì, PriceRange: Dưới 30k)
            (11, 'Bánh Mì Pate Trứng', '25k', 'https://cdn2.fptshop.com.vn/unsafe/Uploads/images/tin-tuc/176285/Originals/cach-lam-banh-mi-pate-trung-16.jpg', 'Bánh mì pate gan mịn, trứng ốp la thơm ngon'),
            (11, 'Bánh Mì Thịt Nướng', '28k', 'https://www.huongnghiepaau.com/wp-content/uploads/2019/08/banh-mi-kep-thit-nuong-thom-phuc.jpg', 'Bánh mì thịt nướng thơm lừng, rau sống tươi'),
            (11, 'Bánh Mì Gà Nướng', '28k', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSNoBXctT92LT1Uh-iBQX8WFQCm77fIwDer4A&s', 'Bánh mì gà nướng mật ong, da giòn rụm'),
            (11, 'Bánh Mì Chả Cá', '25k', 'https://cdn.tgdd.vn/Files/2022/11/23/1489610/cach-lam-banh-mi-cha-ca-don-gian-cho-bua-sang-day-dinh-duong-202211301148589006.jpg', 'Bánh mì chả cá Hà Nội, rau thơm đậm đà'),
            (11, 'Bánh Mì Trứng Chả', '25k', 'https://banhmimahai.vn/wp-content/uploads/2021/12/z3250867383290_71f020a0245577f8061325e507b7acb4.jpg', 'Bánh mì trứng và chả lụa truyền thống'),
            -- RestaurantId 12: Bánh Mì Phố (Category: Bánh mì, PriceRange: Dưới 30k)
            (12, 'Bánh Mì Trứng Pate', '25k', 'https://blog.dktcdn.net/files/cach-lam-banh-mi-pate-trung-ngon-de-ban.jpg', 'Bánh mì pate và trứng ốp la, giòn thơm'),
            (12, 'Bánh Mì Thịt Xá Xíu', '28k', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRWYopJWk_WPASem3vPXETOtNEMKmIfvc5b6w&s', 'Bánh mì thịt xá xíu mềm, nước sốt đậm đà'),
                        (12, 'Bánh Mì Chả Lụa', 25000, 'https://giochabobich.com/wp-content/uploads/2022/11/banh-mi-cha-lua.jpg', 'Bánh mì chả lụa Hà Nội, rau sống tươi mát'),
                                                                                                                                                                                                                         (12, 'Bánh Mì Gà Xé', 28000, 'https://i-giadinh.vnecdn.net/2021/10/15/banhmi-1634272764-1554-1634272894.jpg', 'Bánh mì gà xé phay, hành phi thơm'),
                                                                                                                                                                                                                         (12, 'Bánh Mì Heo Quay', 28000, 'https://cdn2.fptshop.com.vn/unsafe/1920x0/filters:format(webp):quality(75)/2024_1_10_638404773506825107_banh-mi-heo-quay-1.jpg', 'Bánh mì heo quay da giòn, nước chấm đặc biệt'),
            
                                                                                                                                                                                                                         -- RestaurantId 13
                                                                                                                                                                                                                         (13, 'Cơm Tấm Sườn Nướng', 45000, 'https://i.ytimg.com/vi/cJu6tFJe_Gc/maxresdefault.jpg', 'Cơm tấm với sườn nướng thơm, nước mắm ớt'),
                                                                                                                                                                                                                         (13, 'Cơm Tấm Chả Trứng', 40000, 'https://cdn.tgdd.vn/2021/07/CookRecipe/GalleryStep/thanh-pham-1489.jpg', 'Cơm tấm chả trứng mềm, kèm dưa chua'),
                                                                                                                                                                                                                         (13, 'Cơm Tấm Gà Nướng', 45000, 'https://file.hstatic.net/200000700229/article/com-tam-ga-nuong-thumb_5945b01705c349e4b01aca9534a53a84.jpg', 'Cơm tấm gà nướng mật ong, đậm vị'),
                                                                                                                                                                                                                         (13, 'Cơm Tấm Thịt Kho', 40000, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnhIf4KzEUvlr17ls2z190R-1dNNKcZs0G0w&s', 'Cơm tấm thịt kho tàu, béo ngậy'),
                                                                                                                                                                                                                         (13, 'Cơm Tấm Cá Kho', 45000, 'https://hd1.hotdeal.vn/images/uploads/2016/02/19/233920/233920-com-tam-body-%20%285%29.jpg', 'Cơm tấm cá kho tộ, vị đậm đà miền Nam'),
            
                                                                                                                                                                                                                         -- RestaurantId 14
                                                                                                                                                                                                                         (14, 'Cơm Gà Hải Nam', 65000, 'https://file.hstatic.net/200000385717/article/bia_6294906d3b774dd7a08e6515512360e2.jpg', 'Cơm gà hấp mềm, kèm nước chấm gừng đặc trưng'),
                                                                                                                                                                                                                         (14, 'Cơm Gà Chiên Mắm', 70000, 'https://i.ytimg.com/vi/s1mYt1Gopks/sddefault.jpg?v=6415833d', 'Cơm với gà chiên giòn, sốt mắm tỏi thơm'),
                                                                                                                                                                                                                         (14, 'Cơm Gà Nướng Mật Ong', 75000, 'https://product.hstatic.net/200000523823/product/com_dui_ga_nuong_mat_ong__2__45de1d3f966c4245831c562b3e3a0b7f_1024x1024.png', 'Cơm gà nướng mật ong, da giòn, thịt ngọt'),
                                                                                                                                                                                                                         (14, 'Gỏi Gà Xé Phay', 60000, 'https://bepxua.vn/wp-content/uploads/2022/11/cach-lam-goi-ga-xe-phay.jpg', 'Gỏi gà xé với rau răm, hành phi, nước mắm chua ngọt'),
                                                                                                                                                                                                                         (14, 'Canh Gà Lá Giang', 40000, 'https://www.huongnghiepaau.com/wp-content/uploads/2018/07/canh-ga-la-giang-mang-chua.jpg', 'Canh chua lá giang nấu với gà ta, thanh mát'),
            
                                                                                                                                                                                                                         -- RestaurantId 15
                                                                                                                                                                                                                         (15, 'Cơm Niêu Cá Kho Tộ', 75000, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSKG8gkTK6yCsAOiO-_q3CDRsHXReznzomatg&s', 'Cơm niêu với cá kho tộ đậm đà, thơm lừng'),
                                                                                                                                                                                                                         (15, 'Cơm Niêu Thịt Kho Tàu', 70000, 'https://pho10lyquocsu.com.vn/watermark/product/540x540x1/upload/product/z2343709718900eb9551d9469ebdfdb1a21542eaefc52e-8294.jpg', 'Cơm niêu thịt kho tàu, béo ngậy, mềm thơm'),
                                                                                                                                                                                                                         (15, 'Cơm Niêu Gà Nướng', 75000, 'https://kombo.vn/medias/2022/09/con-nieu-ga-nuong-sot-teriyaki-kombo.jpg', 'Cơm niêu gà nướng sả ớt, vị đậm đà'),
                                                                                                                                                                                                                         (15, 'Canh Cua Rau Đay', 45000, 'https://cdn.tgdd.vn/2021/09/CookProduct/1200-1200x676-45.jpg', 'Canh cua rau đay ngọt mát, bổ dưỡng'),
                                                                                                                                                                                                                         (15, 'Gỏi Ngó Sen Tôm Thịt', 65000, 'https://cdn.netspace.edu.vn/images/2018/10/26/cach-lam-goi-ngo-sen-ngon-khong-dung-dua-800.jpg', 'Gỏi ngó sen tôm thịt, chua ngọt hài hòa'),
            
                                                                                                                                                                                                                         -- RestaurantId 16
                                                                                                                                                                                                                         (16, 'Cơm Rang Dưa Bò', 45000, 'https://afamilycdn.com/150157425591193600/2023/12/17/cong-thuc-lam-com-rang-dua-bo-ngon-chuan-vi-ha-noi1-1702799295172-17027992956191958849824.jpg', 'Cơm rang dưa bò thơm ngon, đậm vị'),
                                                                                                                                                                                                                         (16, 'Cơm Rang Hải Sản', 50000, 'https://www.huongnghiepaau.com/wp-content/uploads/2016/06/mon-com-chien-hai-san.jpg', 'Cơm rang tôm mực tươi, rau củ giòn'),
                                                                                                                                                                                                                         (16, 'Cơm Rang Gà Xé', 40000, 'https://cdn.pastaxi-manager.onepas.vn/content/uploads/articles/huyendt/comchienthitga/C%C6%A1m%20chi%C3%AAn%20th%E1%BB%8Bt%20g%C3%A0%2010.png', 'Cơm rang gà xé, hành phi thơm lừng'),
                                                                                                                                                                                                                         (16, 'Cơm Rang Thập Cẩm', 45000, 'https://icdn.dantri.com.vn/thumb_w/680/dansinh/2024/11/23/co-rang-tham-cam-1732340187826.jpg', 'Cơm rang với thịt, tôm, trứng và rau củ'),
                                                                                                                                                                                                                         (16, 'Cơm Rang Đậu Hũ', 40000, 'https://danviet.ex-cdn.com/files/f1/upload/3-2018/images/2018-09-21/1-1537500677-width650height477.jpg', 'Cơm rang đậu hũ, rau củ, thanh nhẹ'),
            
                                                                                                                                                                                                                         -- RestaurantId 17
                                                                                                                                                                                                                         (17, 'Cơm Chay Thập Cẩm', 65000, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTjD3vNMs_KovDsZ4jc3blgzTlyUait6wYKBg&s', 'Cơm chay với đậu hũ, nấm, rau củ đa dạng'),
                                                                                                                                                                                                                         (17, 'Mì Chay Nấm', 60000, 'https://cdn.tgdd.vn/Files/2021/09/15/1382989/hoc-ngay-cach-lam-mi-trung-xao-nam-voi-cong-thuc-sieu-don-gian-202109152054184443.jpg', 'Mì chay với nấm tươi, nước dùng thanh'),
                                                                                                                                                                                                                         (17, 'Đậu Hũ Chiên Sả', 50000, 'https://i.ytimg.com/vi/cv2CfQjZoVY/maxresdefault.jpg', 'Đậu hũ chiên giòn, ướp sả thơm'),
                                                                                                                                                                                                                         (17, 'Nộm Rau Củ Chay', 45000, 'https://i.ytimg.com/vi/QXy9NTJiy7c/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLCDSirNconb4I6SvuQJO94vgtbQBA', 'Nộm rau củ tươi với nước trộn chua ngọt'),
                                                                                                                                                                                                                         (17, 'Canh Nấm Rau Củ', 40000, 'https://cdn.tgdd.vn/2021/04/CookRecipe/Avatar/canh-suon-nam-rau-cu-thumbnail.jpg', 'Canh chay với nấm và rau củ, bổ dưỡng'),
            
                                                                                                                                                                                                                         -- RestaurantId 18
                                                                                                                                                                                                                         (18, 'Cơm Chay Đậu Hũ', 45000, 'https://giadinh.mediacdn.vn/2020/8/11/photo-2-159711113741810869101.jpg', 'Cơm chay đậu hũ kho, rau củ tươi'),
                                                                                                                                                                                                                         (18, 'Bún Chay Nước Lèo', 40000, 'https://i.ytimg.com/vi/49568RNdyl4/maxresdefault.jpg', 'Bún chay với nước lèo thanh, nấm tươi'),
                                                                                                                                                                                                                         (18, 'Chả Chay Chiên', 35000, 'https://i.ytimg.com/vi/RCwJtkSiUUk/maxresdefault.jpg', 'Chả chay chiên giòn, thơm lừng'),
                                                                                                                                                                                                                         (18, 'Gỏi Chay Ngó Sen', 40000, 'https://cdn.tgdd.vn/2020/07/CookProduct/15-1200x676-1.jpg', 'Gỏi ngó sen chay, nước trộn chua ngọt'),
                                                                                                                                                                                                                         (18, 'Canh Chay Bí Đỏ', 35000, 'https://nolimitcooking.com/wp-content/uploads/2024/10/462545553_1105288667605260_1641121240200901673_n.jpg?w=768', 'Canh bí đỏ chay, ngọt thanh, bổ dưỡng'),
            
                                                                                                                                                                                                                         -- RestaurantId 19
                                                                                                                                                                                                                         (19, 'Cơm Chay Nấm Rơm', 60000, 'https://i.ytimg.com/vi/BEaBeuzCcK4/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLAgbo6d3adDLeHTWMiSjCC2w9Qysg', 'Cơm chay với nấm rơm kho, đậm vị'),
                                                                                                                                                                                                                         (19, 'Mì Chay Tương Đen', 55000, 'https://i.ytimg.com/vi/KPk_qRrBBpY/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLANSTyLfmb3-5tinyCoXe--7UEe1Q', 'Mì chay với sốt tương đen, rau củ tươi'),
                                                                                                                                                                                                                         (19, 'Đậu Hũ Nhồi Nấm', 50000, 'https://cdn.tgdd.vn/2021/12/CookDish/cach-lam-mon-dau-hu-nhoi-nam-thom-ngon-thanh-dam-cho-bua-com-avt-1200x676.jpg', 'Đậu hũ nhồi nấm hương, chiên giòn'),
                                                                                                                                                                                                                         (19, 'Nộm Chay Bắp Chuối', 45000, 'https://vilai.vn/mediacenter/media/images/1827/news/ava/s1000_1000/cach-lam-nom-hoa-chuoi-chay-1532486728.png', 'Nộm bắp chuối chay, chua ngọt hài hòa'),
                                                                                                                                                                                                                         (19, 'Canh Chay Rau Củ', 40000, 'https://cdn.tgdd.vn/Files/2020/11/25/1309128/tong-hop-15-mon-canh-chay-ngon-mieng-de-nau-cho-ngay-ram-202011251151046495.jpg', 'Canh rau củ chay, thanh nhẹ, bổ dưỡng'),
            
                                                                                                                                                                                                                         -- RestaurantId 20
                                                                                                                                                                                                                         (20, 'Cơm Chay Nấm Bào Ngư', 65000, 'https://i.ytimg.com/vi/n3CQQxKuouU/maxresdefault.jpg', 'Cơm chay với nấm bào ngư xào, thơm ngon'),
                                                                                                                                                                                                                         (20, 'Bún Chay Huế', 60000, 'https://khamphahue.com.vn/Portals/0/Medias/Nam2018/T7/Bun-chay-Hue.jpg', 'Bún chay kiểu Huế, nước dùng đậm đà'),
                                                                                                                                                                                                                         (20, 'Đậu Hũ Kho Tương', 55000, 'https://cdn.tgdd.vn/2021/09/CookRecipe/Avatar/dau-hu-kho-tuong-hot-chay-thumbnail.jpg', 'Đậu hũ kho tương, mềm thơm, đậm vị'),
                                                                                                                                                                                                                         (20, 'Gỏi Chay Mít Non', 50000, 'https://cdn.tgdd.vn/Files/2021/09/03/1380015/cach-lam-goi-mit-chay-thom-ngon-don-gian-doi-vi-cho-ngay-ram-tai-nha-202201051420530317.jpg', 'Gỏi mít non chay, nước trộn chua ngọt'),
                                                                                                                                                                                                                         (20, 'Canh Chay Nấm Kim Châm', 45000, 'https://cdn.tgdd.vn/Files/2020/06/27/1266043/doi-gio-voi-mon-canh-nam-chay-ngon-ngot-bo-duong-202006272242559000.jpg', 'Canh nấm kim châm chay, thanh mát'),
            
            
            
            
            
            
            -- 21. Nướng Nhà Gỗ
            (21, 'Ba chỉ bò Mỹ nướng', '120k', 'https://gofoodmarket.vn/wp-content/uploads/2022/09/ba-chi-bo-nuong-anh-thumb.jpg', 'Ba chỉ bò Mỹ thái lát mỏng nướng trên than hoa'),
            (21, 'Sườn nướng mật ong', '110k', 'https://th.bing.com/th/id/R.1a3d6b290d409a4241eb5d81af1722c9?rik=9e99AxuXByNkKw&pid=ImgRaw&r=0&sres=1&sresct=1', 'Sườn heo nướng mật ong thơm lừng'),
            (21, 'Gà nướng lá chanh', '90k', 'https://th.bing.com/th/id/OIP.jKw47GdwlpE-2FjRWZ8WGQAAAA?r=0&o=7rm=3&rs=1&pid=ImgDetMain&o=7&rm=3', 'Gà nướng lá chanh đặc biệt'),
            (21, 'Bạch tuộc nướng sa tế', '140k', 'https://bing.com/th?id=OSK.42535b3eac2f47ad116b59aba3eb9641', 'Bạch tuộc tươi nướng sa tế cay'),
            (21, 'Rau củ nướng', '50k', 'https://tse4.mm.bing.net/th/id/OIP.BN7LaYT40HEzqXwdEWLhXgHaHa?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Rau củ tươi nướng'),
            (21, 'Khoai tây chiên', '35k', 'https://ngonaz.com/wp-content/uploads/2021/10/cach-lam-khoai-tay-chien-4.jpg', 'Khoai tây chiên giòn'),
            
            -- 22. Nướng Lão Ngư
            (22, 'Cá lăng nướng', '180k', 'https://tse3.mm.bing.net/th/id/OIP.YHQN6nzKicR-LXq41TX96gHaE8?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Cá lăng tươi nướng riềng mẻ'),
            (22, 'Ba chỉ bò Mỹ nướng', '120k', 'https://tse3.mm.bing.net/th/id/OIP.eBmjPN1gmkCPTk-VxTo5AQHaD4?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Ba chỉ bò Mỹ thái lát mỏng nướng'),
            (22, 'Sườn nướng mật ong', '110k', 'https://bing.com/th?id=OSK.23abf602c31c8d0ab7ff39d5409fb267', 'Sườn heo nướng mật ong thơm lừng'),
            (22, 'Gà nướng lá chanh', '90k', 'https://bing.com/th?id=OSK.a775f527807057264a84344ea585e15f', 'Gà nướng lá chanh đặc biệt'),
            (22, 'Rau củ nướng', '50k', 'https://tse3.mm.bing.net/th/id/OIP.jHNYPq3K-Ybw5Uih2DM6xgHaDk?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Rau củ tươi nướng'),
            (22, 'Khoai tây chiên', '35k', 'https://product.hstatic.net/200000582249/product/khoai_tay_chien_9572ca98e77744ae9d608e7350096be2_master.jpg', 'Khoai tây chiên giòn'),
            
            -- 23. Nướng Hẻm
            (23, 'Ba chỉ nướng', '80k', 'https://tse3.mm.bing.net/th/id/OIP.-UfIh0A2vsWe4a75uTgvdwHaFj?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Ba chỉ heo nướng than hoa'),
            (23, 'Sườn nướng', '90k', 'https://bing.com/th?id=OSK.cc5513deaa553ee50dedce8ec892371d', 'Sườn heo nướng thơm lừng'),
            (23, 'Gà nướng', '75k', 'https://bing.com/th?id=OSK.6e231b54d7f678d88c7bf2f59bdf9036', 'Gà nướng nguyên con'),
            (23, 'Bạch tuộc nướng', '120k', 'https://tse3.mm.bing.net/th/id/OIP.UXKMiEEr1G1DV6Rsw_WWrwHaD4?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Bạch tuộc tươi nướng sa tế'),
            (23, 'Rau củ nướng', '40k', 'https://tse2.mm.bing.net/th/id/OIP.pLsKuvxCbPRyonlGPKeYKwHaFN?r=0&w=710&h=500&rs=1&pid=ImgDetMain&o=7&rm=3', 'Rau củ tươi nướng'),
            (23, 'Khoai lang nướng', '30k', 'https://tse3.mm.bing.net/th/id/OIP.cz3YYMAXG_wIiIB2_Fl2vwHaHa?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Khoai lang nướng mật'),
            
            -- 24. Nướng Ông Mười
            (24, 'Tôm nướng muối ớt', '130k', 'https://th.bing.com/th/id/R.9a7a8a5216d11955cf616ed61fc4ac97?rik=SfxKy1PFQu%2fpyw&pid=ImgRaw&r=0', 'Tôm tươi nướng muối ớt cay'),
            (24, 'Bạch tuộc nướng', '120k', 'https://tse2.mm.bing.net/th/id/OIP.yvvEzeqSq4s8NsbZ30rqDQHaDO?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Bạch tuộc tươi nướng sa tế'),
            (24, 'Ba chỉ bò Mỹ nướng', '120k', 'https://gofood.vn/uploads/Anh/ba-chi-bo-my-nuong3.jpg', 'Ba chỉ bò Mỹ thái lát mỏng nướng'),
            (24, 'Sườn nướng mật ong', '110k', 'https://bing.com/th?id=OSK.ebc845f65609d7003ed35614923532b2', 'Sườn heo nướng mật ong thơm lừng'),
            (24, 'Rau củ nướng', '50k', 'https://blog.onelife.vn/wp-content/uploads/2023/08/ddd73d80-3.png', 'Rau củ tươi nướng'),
            (24, 'Khoai tây chiên', '35k', 'https://tse2.mm.bing.net/th/id/OIP._5wkud0zUQwwf0bCi970MwHaHa?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Khoai tây chiên giòn'),
            
            -- 25. Lẩu Thái Hải Sản
            (25, 'Lẩu Thái hải sản', '250k', 'https://cdn.nguyenkimmall.com/images/companies/_1/tin-tuc/kinh-nghiem-meo-hay/n%E1%BA%A5u%20%C4%83n/nau-lau-thai-chuan-vi-ngon-nhu-the-nao.jpg', 'Lẩu Thái chua cay, hải sản tươi'),
            (25, 'Tôm sú tươi', '120k', 'https://ruoctom.com/wp-content/uploads/2019/11/tom-su.jpg', 'Tôm sú tươi sống'),
            (25, 'Mực trứng', '90k', 'https://tse4.mm.bing.net/th/id/OIP.yOy_FqH4DOWSqEsG8_bf_wHaE8?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Mực trứng tươi ngon'),
            (25, 'Ngao hấp', '60k', 'https://th.bing.com/th/id/OIP.F4Ql_tNm9MBwc6g8ATqvKAHaEK?r=0&o=7rm=3&rs=1&pid=ImgDetMain&o=7&rm=3', 'Ngao hấp sả ớt'),
            (25, 'Rau nhúng lẩu', '50k', 'https://tse4.mm.bing.net/th/id/OIP.xXsSibldAc7bGg0a6XVbxAHaE-?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Rau củ tươi nhúng lẩu'),
            (25, 'Bún tươi', '20k', 'https://tse4.mm.bing.net/th/id/OIP.suQ6sWFUvbq9I5ul9NLGrQHaFl?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Bún tươi ăn kèm lẩu'),
            
            -- 26. Lẩu Phố
            (26, 'Lẩu hải sản', '220k', 'https://tse1.mm.bing.net/th/id/OIP.fRk8Thk_xn4NF8ZB2lo4YgHaFa?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Lẩu hải sản tươi sống'),
            (26, 'Lẩu thập cẩm', '210k', 'https://th.bing.com/th/id/OIP.U7Mff8rI1sfYk9qouBfnjwHaFj?r=0&o=7rm=3&rs=1&pid=ImgDetMain&o=7&rm=3', 'Lẩu thập cẩm đa dạng topping'),
            (26, 'Bò Mỹ nhúng lẩu', '150k', 'https://tse1.mm.bing.net/th/id/OIP.fnTdL6WRL-LDrnzyGbjonAHaGB?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Bò Mỹ thái lát mỏng nhúng lẩu'),
            (26, 'Rau nhúng lẩu', '50k', 'https://th.bing.com/th/id/R.c57b1289b95d01cedb1a0d1ae9755bc4?rik=7eOBbLOaA6USEQ&pid=ImgRaw&r=0', 'Rau củ tươi nhúng lẩu'),
            (26, 'Mì tôm', '15k', '', 'Mì tôm ăn kèm lẩu'),
            (26, 'Nước ngọt', '15k', '', 'Nước giải khát các loại'),
            
            -- 27. Lẩu Nấm Ashima
            (27, 'Lẩu nấm Ashima', '280k', 'https://halotravel.vn/wp-content/uploads/2021/05/lau-nam-ashima-ha-noi-5.jpg', 'Lẩu nấm cao cấp, nước dùng ngọt thanh'),
            (27, 'Nấm đông cô', '60k', 'https://tse3.mm.bing.net/th/id/OIP.dl80AENplWXf5E_pwJvSTwHaEo?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Nấm đông cô tươi'),
            (27, 'Nấm kim châm', '50k', 'https://th.bing.com/th/id/R.fa8776ccaa73a9c4b094ccec4b00dead?rik=AmZ5Pb9RP%2bESLA&pid=ImgRaw&r=0', 'Nấm kim châm tươi'),
            (27, 'Đậu hũ non', '30k', 'https://tse4.mm.bing.net/th/id/OIP.pYRW3OS5c4dcyYjYU_VuYwHaEe?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Đậu hũ non mềm mịn'),
            (27, 'Rau nhúng lẩu', '50k', 'https://th.bing.com/th/id/R.c57b1289b95d01cedb1a0d1ae9755bc4?rik=7eOBbLOaA6USEQ&pid=ImgRaw&r=0', 'Rau củ tươi nhúng lẩu'),
            (27, 'Bún tươi', '20k', 'https://cdn.tgdd.vn/2020/07/CookProductThumb/Untitled-1-620x620-44.jpg', 'Bún tươi ăn kèm lẩu'),
            
            -- 28. Lẩu Bò Nhúng Dấm
            (28, 'Lẩu bò nhúng dấm', '200k', 'https://tse4.mm.bing.net/th/id/OIP.xfmvHUXdA1Qe_CVceZY4TAHaFj?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Lẩu bò nhúng dấm đặc biệt'),
            (28, 'Bò Mỹ thái lát', '150k', 'https://lavietfood.com/wp-content/uploads/2020/10/z2125559443174_d63d06007e1d4a1c1a871cec7427e382.jpg', 'Bò Mỹ thái lát mỏng nhúng lẩu'),
            (28, 'Gân bò', '90k', 'https://hunggiaco.com/upload/images/gan-bo-nhap-khau.jpg', 'Gân bò tươi ngon'),
            (28, 'Rau nhúng lẩu', '50k', 'https://th.bing.com/th/id/R.c57b1289b95d01cedb1a0d1ae9755bc4?rik=7eOBbLOaA6USEQ&pid=ImgRaw&r=0', 'Rau củ tươi nhúng lẩu'),
            (28, 'Bún tươi', '20k', 'https://cdn.tgdd.vn/2020/07/CookProductThumb/Untitled-1-620x620-44.jpg', 'Bún tươi ăn kèm lẩu'),
            (28, 'Nước ngọt', '15k', 'https://cdn.tgdd.vn/2021/05/content/1-800x450-54.jpg', 'Nước giải khát các loại'),
            
            -- 29. Xôi Yến
            (29, 'Xôi xéo chả quế', '30k', 'https://tse2.mm.bing.net/th/id/OIP.6kwr615u3MF0_3ujxcIsdQHaEo?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Xôi xéo mềm dẻo, chả quế thơm lừng'),
            (29, 'Xôi pate trứng', '28k', 'https://tse1.mm.bing.net/th/id/OIP.Jjr6ZqJCi7yt4e3TVdidhQHaEK?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Xôi pate, trứng béo ngậy'),
            (29, 'Xôi gà xé', '35k', 'https://tse4.mm.bing.net/th/id/OIP.zq9TxHNG39wdT3UvtnALXwHaEK?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Xôi gà xé thơm ngon'),
            (29, 'Xôi ruốc', '25k', 'https://tse1.mm.bing.net/th/id/OIP.J9Z7m3-XlDeCTYzJ9PriegAAAA?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Xôi ruốc mặn mà'),
            (29, 'Chè đậu xanh', '12k', 'https://th.bing.com/th/id/R.9c95b07378e9e72db5ee81e72b66d961?rik=y6aYu%2fTXKqrrgw&pid=ImgRaw&r=0', 'Chè đậu xanh ngọt nhẹ'),
            (29, 'Sữa đậu', '10k', 'https://static.vinwonders.com/production/sua-dau-nanh-da-lat-banner.jpg', 'Sữa đậu nành tươi'),
            
            -- 30. Xôi Gà Bà Hồi
            (30, 'Xôi gà xé', '32k', 'https://i.ytimg.com/vi/gZt9CsZCpDM/maxresdefault.jpg', 'Xôi gà xé truyền thống'),
            (30, 'Xôi gà quay', '38k', 'https://media.mia.vn/uploads/blog-du-lich/thu-ngay-ga-quay-xoi-phong-binh-duong-thom-ngon-nuc-no-2-1654823886.jpeg', 'Xôi gà quay da giòn'),
            (30, 'Xôi chả', '28k', 'https://tse4.mm.bing.net/th/id/OIP.Lg2lMPIWUE-gcUrd0oFg3AHaGc?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Xôi chả thơm ngon'),
            (30, 'Xôi ruốc', '25k', 'https://tse1.mm.bing.net/th/id/OIP.J9Z7m3-XlDeCTYzJ9PriegAAAA?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Xôi ruốc mặn mà'),
            (30, 'Chè đậu xanh', '12k', 'https://tse1.mm.bing.net/th/id/OIP.cYRoMh8CpdprxPgwkEL87QHaEK?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Chè đậu xanh ngọt nhẹ'),
            (30, 'Sữa đậu', '10k', 'https://th.bing.com/th/id/R.a09d3b77fc5631322ab41b6662bd90ec?rik=Tqtj%2bdojcKVMVQ&pid=ImgRaw&r=0', 'Sữa đậu nành tươi'),
            
            -- 31. Xôi Xéo Bà Hạt
            (31, 'Xôi xéo', '25k', 'https://th.bing.com/th/id/R.2a19c1c524a3492b20535f931e47c73e?rik=4gug4pERz5q0WQ&pid=ImgRaw&r=0', 'Xôi xéo truyền thống, thơm ngon'),
            (31, 'Xôi chả', '28k', 'https://tse4.mm.bing.net/th/id/OIP.Lg2lMPIWUE-gcUrd0oFg3AHaGc?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Xôi chả ăn kèm xôi xéo'),
            (31, 'Xôi pate', '30k', 'https://tse1.mm.bing.net/th/id/OIP.Jjr6ZqJCi7yt4e3TVdidhQHaEK?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Xôi pate béo ngậy'),
            (31, 'Xôi ruốc', '25k', 'https://tse1.mm.bing.net/th/id/OIP.J9Z7m3-XlDeCTYzJ9PriegAAAA?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Xôi ruốc mặn mà'),
            (31, 'Chè đậu xanh', '12k', 'https://tse1.mm.bing.net/th/id/OIP.cYRoMh8CpdprxPgwkEL87QHaEK?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Chè đậu xanh ngọt nhẹ'),
            (31, 'Sữa đậu', '10k', 'https://th.bing.com/th/id/R.a09d3b77fc5631322ab41b6662bd90ec?rik=Tqtj%2bdojcKVMVQ&pid=ImgRaw&r=0', 'Sữa đậu nành tươi'),
            
            -- 32. Xôi Lạc
            (32, 'Xôi thập cẩm', '30k', 'https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEgjuZzyCyLS9AZA7zWFcYHGbVRokDoqPIn1CDGaKRnhAd62d2tjaLt9Vw1a4Cp6Rf3uKTVVVaaLCNTifChXIq83yaKIfU5U7Ny4Zpz3kDIFo6OwOYmTf5RXtg6PGc1M2qMhwP61k_coe_0U0Y1iXu7twf0dxLWlcjARL_rVoL46mKKepFlAP5WEwO0moA/w1200-h630-p-k-no-nu/xoi%20man%20thap%20cam.jpg', 'Xôi thập cẩm nhiều topping'),
            (32, 'Xôi gà xé', '35k', 'https://i.ytimg.com/vi/gZt9CsZCpDM/maxresdefault.jpg', 'Xôi gà xé thơm ngon'),
            (32, 'Xôi chả', '28k', 'https://tse4.mm.bing.net/th/id/OIP.Lg2lMPIWUE-gcUrd0oFg3AHaGc?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Xôi chả ăn kèm xôi thập cẩm'),
            (32, 'Xôi pate', '30k', 'https://tse1.mm.bing.net/th/id/OIP.Jjr6ZqJCi7yt4e3TVdidhQHaEK?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Xôi pate béo ngậy'),
            (32, 'Chè đậu xanh', '12k', 'https://tse1.mm.bing.net/th/id/OIP.cYRoMh8CpdprxPgwkEL87QHaEK?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Chè đậu xanh ngọt nhẹ'),
            (32, 'Sữa đậu', '10k', 'https://th.bing.com/th/id/R.a09d3b77fc5631322ab41b6662bd90ec?rik=Tqtj%2bdojcKVMVQ&pid=ImgRaw&r=0', 'Sữa đậu nành tươi'),
            
            -- 33. Mì Vằn Thắn Hải Nam
            (33, 'Mì vằn thắn nước', '40k', 'https://halotravel.vn/wp-content/uploads/2020/08/mi-van-than-ha-noi-10.jpg', 'Mì vằn thắn nước truyền thống'),
            (33, 'Mì vằn thắn khô', '42k', 'https://toplist.vn/images/800px/phuc-duc-quan-271959.jpg', 'Mì vằn thắn trộn sốt đặc biệt'),
            (33, 'Hoành thánh chiên', '30k', 'https://tse4.mm.bing.net/th/id/OIP.s3tISGOLJSyq7r2p1AZZWgHaEK?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Hoành thánh chiên giòn'),
            (33, 'Sủi cảo hấp', '35k', 'https://tse3.mm.bing.net/th/id/OIP.XJYH551OCF3WupJM8Y2MNAHaEo?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Sủi cảo hấp nhân tôm thịt'),
            (33, 'Trà đá', '5k', 'https://afamilycdn.com/150157425591193600/2023/6/17/3303817075387573581227292298016545177830025n-16869708320621700640314-1686991585482-1686991585658246030573.jpg', 'Thức uống giải khát'),
            (33, 'Nước sâm', '10k', 'https://tiensinuoc.com/wp-content/uploads/2022/06/nuoc-sam-0.jpg', 'Giải nhiệt ngày hè'),
            
            -- 34. Mì Trộn Indomie
            (34, 'Mì trộn Indomie bò', '35k', 'https://winmart.onl/assets/media/images/products/07531/mytronbovi_1105_175733_Y4SqNL.png', 'Mì trộn vị bò kiểu Indonesia'),
            (34, 'Mì trộn Indomie gà', '35k', 'https://winmart.onl/assets/media/images/products/07531/mytronbovi_1105_175733_Y4SqNL.png', 'Mì trộn vị gà cay'),
            (34, 'Mì trộn xúc xích', '38k', 'https://winmart.onl/assets/media/images/products/07531/mytronbovi_1105_175733_Y4SqNL.png', 'Mì trộn xúc xích chiên giòn'),
            (34, 'Mì trộn trứng', '30k', 'https://winmart.onl/assets/media/images/products/07531/mytronbovi_1105_175733_Y4SqNL.png', 'Mì trộn trứng lòng đào'),
            (34, 'Trà sữa', '25k', 'https://khoinguonsangtao.vn/wp-content/uploads/2022/10/hinh-anh-tra-sua-full-topping-1.jpg', 'Trà sữa homemade'),
            (34, 'Nước cam', '20k', 'https://th.bing.com/th/id/R.f93703966de4c8e764d35a529a8eb8c4?rik=Jf0h07L4IEO9qA&pid=ImgRaw&r=0', 'Nước cam vắt tươi'),
            
            -- 35. Mì Ý Pasta Box
            (35, 'Spaghetti bò bằm', '55k', 'https://tse3.mm.bing.net/th/id/OIP.KT24_4JizXLfq9KF5Vp1_wHaHa?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Mì Ý sốt bò bằm truyền thống'),
            (35, 'Spaghetti hải sản', '65k', 'https://bing.com/th?id=OSK.466fb3f4e8ebff6a7c8c6f37c050c92a', 'Mì Ý sốt hải sản tươi'),
            (35, 'Mì Ý sốt kem gà', '60k', 'https://tse1.mm.bing.net/th/id/OIP.paeoQkN0qZM5kQKNXWHy0wHaET?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Mì Ý sốt kem và thịt gà'),
            (35, 'Mì Ý sốt cà chua', '50k', 'https://bing.com/th?id=OSK.76f968cf5c511b514fb5b6b2db70421c', 'Mì Ý sốt cà chua truyền thống'),
            (35, 'Khoai tây chiên', '30k', 'https://tse3.mm.bing.net/th/id/OIP.9l4lrX1Z-uCXjsyH2GYTrwHaHa?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Khoai tây chiên giòn'),
            (35, 'Trà đào', '20k', 'https://bing.com/th?id=OSK.3bfa3d9a1030f2b0e49c3bc471d855dd', 'Trà đào mát lạnh'),
            
            -- 36. Mì Quảng Bà Mành
            (36, 'Mì Quảng tôm thịt', '45k', 'https://th.bing.com/th/id/R.962f16c5d9e104b828be29b2176a36d1?rik=%2fYJNBRyIcmLFIg&pid=ImgRaw&r=0', 'Mì Quảng tôm thịt đặc sản Đà Nẵng'),
            (36, 'Mì Quảng gà', '45k', 'https://th.bing.com/th/id/R.962f16c5d9e104b828be29b2176a36d1?rik=%2fYJNBRyIcmLFIg&pid=ImgRaw&r=0', 'Mì Quảng gà ta thơm ngon'),
            (36, 'Mì Quảng cá lóc', '50k', 'https://th.bing.com/th/id/R.962f16c5d9e104b828be29b2176a36d1?rik=%2fYJNBRyIcmLFIg&pid=ImgRaw&r=0', 'Mì Quảng cá lóc đồng'),
            (36, 'Bánh tráng mè', '10k', 'https://tse4.mm.bing.net/th/id/OIP.YMalI-cOs9E6xJK0tM1m9wHaE7?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Bánh tráng mè nướng giòn'),
            (36, 'Trà đá', '5k', 'https://tse2.mm.bing.net/th/id/OIP.uqi79OnEc1BKGZhl9IR3gwHaE7?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Thức uống giải khát'),
            (36, 'Nước mơ', '12k', 'https://tse4.mm.bing.net/th/id/OIP.NZDUy9XXk-JTHuabmgtsNAHaHa?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Nước mơ truyền thống'),
            
            -- 37. Trà Sữa Nhà Làm
            (37, 'Trà sữa truyền thống', '25k', 'https://th.bing.com/th/id/R.8ed6e945190e835fb20698281ca17b7e?rik=%2fCpcMGAMaNHz9Q&pid=ImgRaw&r=0', 'Trà sữa homemade vị truyền thống'),
            (37, 'Trà sữa trân châu đen', '28k', 'https://th.bing.com/th/id/R.8ed6e945190e835fb20698281ca17b7e?rik=%2fCpcMGAMaNHz9Q&pid=ImgRaw&r=0', 'Trà sữa trân châu đen dẻo dai'),
            (37, 'Trà đào cam sả', '30k', 'https://tse2.mm.bing.net/th/id/OIP.otbytoEHejkBSa-_icyMLAHaE8?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Trà đào cam sả mát lạnh'),
            (37, 'Trà xanh matcha', '32k', 'https://tse4.mm.bing.net/th/id/OIP.LGPi1LwdEiz5Goae86hAzwHaE8?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Trà xanh matcha Nhật Bản'),
            (37, 'Sữa tươi trân châu đường đen', '35k', 'https://tse3.mm.bing.net/th/id/OIP.3nyxfgj0pdolRDeoAKwPBgHaE8?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Sữa tươi trân châu đường đen thơm ngậy'),
            (37, 'Bánh flan', '15k', 'https://tse3.mm.bing.net/th/id/OIP.mTWiJBEkr1zkCec3iYqq9gHaD4?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Bánh flan mềm mịn'),
            
            -- 38. Nước Mía Tạ Hiện
            (38, 'Nước mía truyền thống', '15k', 'https://tse1.mm.bing.net/th/id/OIP.gc0WuIeIeVHn232Zr9OOAwHaEl?r=0&w=825&h=510&rs=1&pid=ImgDetMain&o=7&rm=3', 'Nước mía tươi nguyên chất'),
            (38, 'Nước mía tắc', '17k', 'https://tse1.mm.bing.net/th/id/OIP.gc0WuIeIeVHn232Zr9OOAwHaEl?r=0&w=825&h=510&rs=1&pid=ImgDetMain&o=7&rm=3', 'Nước mía pha tắc mát lạnh'),
            (38, 'Nước mía cam', '20k', 'https://tse1.mm.bing.net/th/id/OIP.gc0WuIeIeVHn232Zr9OOAwHaEl?r=0&w=825&h=510&rs=1&pid=ImgDetMain&o=7&rm=3', 'Nước mía pha cam tươi'),
            (38, 'Nước mía dứa', '20k', 'https://tse1.mm.bing.net/th/id/OIP.gc0WuIeIeVHn232Zr9OOAwHaEl?r=0&w=825&h=510&rs=1&pid=ImgDetMain&o=7&rm=3', 'Nước mía pha dứa thơm ngon'),
            (38, 'Bánh mì pate', '20k', 'https://tse1.mm.bing.net/th/id/OIP.IxSQxenayDYM2oZcHwj7PgHaEo?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Bánh mì pate ăn kèm nước mía'),
            (38, 'Bánh bao chiên', '18k', 'https://bing.com/th?id=OSK.2f836e7679e3ecd104e3d104874de738', 'Bánh bao chiên nóng giòn'),
            
            -- 39. Nước Ép Trái Cây Tươi
            (39, 'Nước ép cam', '22k', 'https://www.btaskee.com/wp-content/uploads/2023/06/nuoc-ep-cam-tuoi-thom-ngon-tai-nha.jpg', 'Nước ép cam tươi nguyên chất'),
            (39, 'Nước ép dứa', '20k', 'https://toplist.vn/images/800px/nuoc-ep-dua-luoi-thom-dua-692618.jpg', 'Nước ép dứa thơm mát'),
            (39, 'Nước ép cà rốt', '20k', 'https://th.bing.com/th/id/R.e18dd6cce810052d77e2e6bcedfa1f9f?rik=7cfHx7y4jlfMPA&pid=ImgRaw&r=0', 'Nước ép cà rốt bổ dưỡng'),
            (39, 'Nước ép ổi', '22k', 'https://tse4.mm.bing.net/th/id/OIP.gXkxg6BTKq_4uP6jbB4EUAHaFj?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Nước ép ổi tươi'),
            (39, 'Nước ép táo', '25k', 'https://bing.com/th?id=OSK.5551653279c5957c6103f5de9fa24e49', 'Nước ép táo nhập khẩu'),
            (39, 'Sữa chua đánh đá', '18k', 'https://tse2.mm.bing.net/th/id/OIP.HgqEARaECH0gNa6PZjWeqwHaE8?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Sữa chua đánh đá mát lạnh'),
            
            -- 40. Sinh Tố Cô Ba
            (40, 'Sinh tố bơ', '25k', 'https://tse3.mm.bing.net/th/id/OIP.zfHcvq15eQ2Zrr8i1YflNQAAAA?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Sinh tố bơ sánh mịn'),
            (40, 'Sinh tố xoài', '25k', 'https://barona.vn/storage/meo-vat/184/sinh-to-xoai-chanh-day.jpg', 'Sinh tố xoài tươi mát'),
            (40, 'Sinh tố dâu', '25k', 'https://bing.com/th?id=OSK.72e3fc6ac99e552c3886e614574d038e', 'Sinh tố dâu tây thơm ngon'),
            (40, 'Sinh tố mãng cầu', '28k', 'https://th.bing.com/th/id/OIP.nRIKqpyshXW3HGeP4qMj1wHaFf?r=0&o=7rm=3&rs=1&pid=ImgDetMain&o=7&rm=3', 'Sinh tố mãng cầu chua ngọt'),
            (40, 'Nước ép cam', '22k', 'https://tse1.mm.bing.net/th/id/OIP.svee692Ur8ib3Z6176SW0AHaE8?r=0&rs=1&pid=ImgDetMain&o=7&rm=3', 'Nước ép cam tươi nguyên chất'),
            (40, 'Sữa chua mít', '20k', 'https://cdn.tgdd.vn/Files/2017/10/07/1030949/cach-lam-sua-chua-mit-don-gian-tai-nha-202109161430574873.jpg', 'Sữa chua mít trân châu');
            
            """;


    public static final String SQL_INSERT_TABLE_CATEGORIES = """
INSERT INTO Categories (Name) VALUES ('Cơm'), ('Bún'), ('Phở'), ('Bánh mì'), ('Chay'),('Nướng'),
 ('Lẩu'), ('Xôi'), ('Mì'), ('Nước'); 
""";


    public static final String SQL_INSERT_TABLE_RESTAURANT_CATEGORY = """
                           INSERT INTO RestaurantCategory (RestaurantId, CategoryId) VALUES
            (1, 3),  -- Phở Bát Đàn → Phở
            (2, 3),  -- Phở Thìn Lò Đúc → Phở
            (3, 3),  -- Phở 10 Lý Quốc Sư → Phở
            (4, 3),  -- Phở Gà Bà Nguyệt → Phở
            (5, 2),  -- Bún Chả Đắc Kim → Bún
            (6, 2),  -- Bún riêu Huyền Thu → Bún
            (7, 2),  -- Nét Huế → Bún
            (8, 2),  -- Bún Ốc Bà Lương → Bún
            (9, 4),  -- Bánh Mì 25 → Bánh mì
            (10, 4), -- Bánh Mì Bà Dần → Bánh mì
            (11, 4), -- Bánh Mì P → Bánh mì
            (12, 4), -- Bánh Mì Phố → Bánh mì
            (13, 1), -- Cơm Tấm Sài Gòn → Cơm
            (14, 1), -- Cơm Gà Hải Nam → Cơm
            (15, 1), -- Cơm Niêu Hải Sư → Cơm
            (16, 1), -- Cơm Rang Đức Hạnh → Cơm
            (17, 5), -- Uu Dam Chay → Chay
            (18, 5), -- Chay An Lạc → Chay
            (19, 5), -- Chay Bồ Đề → Chay
            (20, 5), -- Chay Hoa Sen → Chay
            (21, 6), -- Nướng Nhà Gỗ → Nướng
            (22, 6), -- Nướng Lão Ngư → Nướng
            (23, 6), -- Nướng Hẻm → Nướng
            (24, 6), -- Nướng Ông Mười → Nướng
            (25, 7), -- Lẩu Thái Hải Sản → Lẩu
            (26, 7), -- Lẩu Phố → Lẩu
            (27, 7), -- Lẩu Nấm Ashima → Lẩu
            (28, 7), -- Lẩu Bò Nhúng Dấm → Lẩu
            (29, 8), -- Xôi Yến → Xôi
            (30, 8), -- Xôi Gà Bà Hồi → Xôi
            (31, 8), -- Xôi Xéo Bà Hạt → Xôi
            (32, 8), -- Xôi Lạc → Xôi
            (33, 9), -- Mì Vằn Thắn Hải Nam → Mì
            (34, 9), -- Mì Trộn Indomie → Mì
            (35, 9), -- Mì Ý Pasta Box → Mì
            (36, 9), -- Mì Quảng Bà Mành → Mì
            (37, 10), -- Trà Sữa Nhà Làm → Nước
            (38, 10), -- Nước Mía Tạ Hiện → Nước
            (39, 10), -- Nước Ép Trái Cây → Nước
            (40, 10); -- Sinh Tố Cô Ba → Nước
            """;


    public static final String SQL_INSERT_TABLE_REVIEW_MEDIA = """
                INSERT INTO ReviewMedia (ReviewId, MediaUrl, MediaType) VALUES
                (1, 'https://i.imgur.com/buncha_review.jpg', 'image'),
                (2, 'https://i.imgur.com/pho_review.jpg', 'image'),
                (3, 'https://i.imgur.com/banhmi_review.jpg', 'image'),
                (5, 'https://i.imgur.com/comtam_review.jpg', 'image'),
                (6, 'https://i.imgur.com/bunrieu_review.jpg', 'image'),
                (10, 'https://i.imgur.com/cafetrung_review.jpg', 'image');
            """;


    public static final String SQL_DELETE_TABLE_REVIEWS = """
            DROP TABLE IF EXISTS Reviews;
            """;
    public static final String SQL_DELETE_TABLE_FAVORITES = """
            DROP TABLE IF EXISTS Favorites;            
            """;
    public static final String SQL_DELETE_TABLE_RESTAURANTS = """
            DROP TABLE IF EXISTS Restaurants;            
            """;
    public static final String SQL_DELETE_TABLE_USERS = """
            DROP TABLE IF EXISTS Users;            
            """;
    public static final String SQL_DELETE_TABLE_MENUS = """
                DROP TABLE IF EXISTS Menus;
            """;
    public static final String SQL_DELETE_TABLE_RESTAURANT_CATEGORY = """
                DROP TABLE IF EXISTS RestaurantCategory;
            """;
    public static final String SQL_DELETE_TABLE_CATEGORIES = """
                DROP TABLE IF EXISTS Categories;
            """;

    public static final String SENDER_EMAIL = "daom28659@gmail.com";
    public static final String SENDER_PASSWORD = "qjmi nqkd vdri jfsm";

}
