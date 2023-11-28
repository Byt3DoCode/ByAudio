# ByAudio
```
📦 
├─ .gitignore
├─ README.md
├─ app
│  ├─ .gitignore
│  ├─ build.gradle
│  ├─ proguard-rules.pro
│  ├─ schemas
│  │  └─ com.byt3.byaudio.model.AppDatabase
│  │     ├─ 1.json
│  │     └─ 2.json
│  └─ src
│     ├─ androidTest
│     │  └─ java
│     │     └─ com
│     │        └─ byt3
│     │           └─ byaudio
│     │              └─ ExampleInstrumentedTest.java
│     ├─ main
│     │  ├─ AndroidManifest.xml
│     │  ├─ ic_launcher-playstore.png
│     │  ├─ java
│     │  │  └─ com
│     │  │     └─ byt3
│     │  │        └─ byaudio
│     │  │           ├─ controller
│     │  │           │  ├─ ByAudioApplication.java
│     │  │           │  ├─ FolderDetailActivity.java
│     │  │           │  ├─ MainActivity.java
│     │  │           │  ├─ SongDetailActivity.java
│     │  │           │  ├─ SplashActivity.java
│     │  │           │  ├─ adapter
│     │  │           │  │  ├─ PlaylistRecyclerAdapter.java
│     │  │           │  │  ├─ QueueDetailRecyclerAdapter.java
│     │  │           │  │  ├─ SongRecyclerAdapter.java
│     │  │           │  │  └─ ViewPagerAdapter.java
│     │  │           │  ├─ fragment
│     │  │           │  │  ├─ DiscoveryFragment.java
│     │  │           │  │  ├─ PlayerFragment.java
│     │  │           │  │  ├─ PlaylistMenuFragment.java
│     │  │           │  │  ├─ QueueDetailFragment.java
│     │  │           │  │  └─ SearchLocalFragment.java
│     │  │           │  ├─ service
│     │  │           │  │  ├─ ActionReceiver.java
│     │  │           │  │  └─ PlayerService.java
│     │  │           │  └─ viewholder
│     │  │           │     ├─ DraggableSongViewHolder.java
│     │  │           │     ├─ PlaylistViewHolder.java
│     │  │           │     └─ SongViewHolder.java
│     │  │           ├─ model
│     │  │           │  ├─ Album.java
│     │  │           │  ├─ AppDatabase.java
│     │  │           │  ├─ Artist.java
│     │  │           │  ├─ CollectionSongCrossRef.java
│     │  │           │  ├─ Folder.java
│     │  │           │  ├─ Song.java
│     │  │           │  ├─ SongCollection.java
│     │  │           │  ├─ dbhandler
│     │  │           │  │  ├─ AlbumDAO.java
│     │  │           │  │  ├─ ArtistDAO.java
│     │  │           │  │  ├─ CoSosDAO.java
│     │  │           │  │  ├─ FolderDAO.java
│     │  │           │  │  ├─ SoArAlFoDAO.java
│     │  │           │  │  ├─ SongCollectionDAO.java
│     │  │           │  │  └─ SongDAO.java
│     │  │           │  └─ objrelation
│     │  │           │     ├─ CollectionWithSongs.java
│     │  │           │     └─ SongAndArtistAndAlbumAndFolder.java
│     │  │           └─ utils
│     │  │              └─ functions.java
│     │  └─ res
│     │     ├─ drawable-hdpi
│     │     │  └─ icon_notification_cat.png
│     │     ├─ drawable-mdpi
│     │     │  └─ icon_notification_cat.png
│     │     ├─ drawable-v24
│     │     │  └─ ic_launcher_foreground.xml
│     │     ├─ drawable-xhdpi
│     │     │  └─ icon_notification_cat.png
│     │     ├─ drawable-xxhdpi
│     │     │  └─ icon_notification_cat.png
│     │     ├─ drawable-xxxhdpi
│     │     │  └─ icon_notification_cat.png
│     │     ├─ drawable
│     │     │  ├─ gradient_splash.xml
│     │     │  ├─ ic_bnav_discovery.png
│     │     │  ├─ ic_bnav_player.png
│     │     │  ├─ ic_bnav_playlist.png
│     │     │  ├─ ic_bnav_queue.png
│     │     │  ├─ ic_bnav_search.png
│     │     │  ├─ ic_cat.png
│     │     │  ├─ ic_close_24.xml
│     │     │  ├─ ic_drag_handle_24.xml
│     │     │  ├─ ic_favorite_24.xml
│     │     │  ├─ ic_folder_24.xml
│     │     │  ├─ ic_forward_24.xml
│     │     │  ├─ ic_history_24.xml
│     │     │  ├─ ic_info_24.xml
│     │     │  ├─ ic_keyboard_arrow_down_24.xml
│     │     │  ├─ ic_launcher_background.xml
│     │     │  ├─ ic_list_24.xml
│     │     │  ├─ ic_more_horiz_24.xml
│     │     │  ├─ ic_more_vert_24.xml
│     │     │  ├─ ic_next_24.xml
│     │     │  ├─ ic_pause_24.xml
│     │     │  ├─ ic_play_24.xml
│     │     │  ├─ ic_previous_24.xml
│     │     │  ├─ ic_rewind_24.xml
│     │     │  ├─ ic_search_24.xml
│     │     │  ├─ navigation_view_colored.xml
│     │     │  └─ round_btn.xml
│     │     ├─ layout
│     │     │  ├─ activity_folder_detail.xml
│     │     │  ├─ activity_main.xml
│     │     │  ├─ activity_song_detail.xml
│     │     │  ├─ activity_splash.xml
│     │     │  ├─ fragment_discovery.xml
│     │     │  ├─ fragment_player.xml
│     │     │  ├─ fragment_playlist_menu.xml
│     │     │  ├─ fragment_queue_detail.xml
│     │     │  ├─ fragment_search_local.xml
│     │     │  ├─ item_draggable_song.xml
│     │     │  ├─ item_history.xml
│     │     │  ├─ item_playlist.xml
│     │     │  └─ item_song.xml
│     │     ├─ menu
│     │     │  └─ menu_bottom_navigation.xml
│     │     ├─ mipmap-anydpi-v26
│     │     │  ├─ ic_launcher.xml
│     │     │  └─ ic_launcher_round.xml
│     │     ├─ mipmap-hdpi
│     │     │  ├─ ic_launcher.webp
│     │     │  ├─ ic_launcher_foreground.webp
│     │     │  └─ ic_launcher_round.webp
│     │     ├─ mipmap-mdpi
│     │     │  ├─ ic_launcher.webp
│     │     │  ├─ ic_launcher_foreground.webp
│     │     │  └─ ic_launcher_round.webp
│     │     ├─ mipmap-xhdpi
│     │     │  ├─ ic_launcher.webp
│     │     │  ├─ ic_launcher_foreground.webp
│     │     │  └─ ic_launcher_round.webp
│     │     ├─ mipmap-xxhdpi
│     │     │  ├─ ic_launcher.webp
│     │     │  ├─ ic_launcher_foreground.webp
│     │     │  └─ ic_launcher_round.webp
│     │     ├─ mipmap-xxxhdpi
│     │     │  ├─ ic_launcher.webp
│     │     │  ├─ ic_launcher_foreground.webp
│     │     │  └─ ic_launcher_round.webp
│     │     ├─ raw
│     │     │  └─ lemminofirecracker.flac
│     │     ├─ values-night
│     │     │  └─ themes.xml
│     │     ├─ values
│     │     │  ├─ colors.xml
│     │     │  ├─ dimens.xml
│     │     │  ├─ ic_launcher_background.xml
│     │     │  ├─ strings.xml
│     │     │  └─ themes.xml
│     │     └─ xml
│     │        ├─ backup_rules.xml
│     │        └─ data_extraction_rules.xml
│     └─ test
│        └─ java
│           └─ com
│              └─ byt3
│                 └─ byaudio
│                    └─ ExampleUnitTest.java
├─ build.gradle
├─ gradle.properties
├─ gradle
│  └─ wrapper
│     ├─ gradle-wrapper.jar
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
└─ settings.gradle
```
©generated by [Project Tree Generator](https://woochanleee.github.io/project-tree-generator)
