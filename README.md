# NexusBridge (Bedrock ↔ Java)

ปลั๊กอินสำหรับทำระบบเชื่อม **Bedrock ↔ Java** ระดับ production scaffold โดยเน้น 3 ส่วนสำคัญ:

- ระบบคอนเวิร์สไอเท็ม Java ↔ Bedrock ที่ขยายได้
- ระบบแมป texture/custom-model-data สำหรับ custom content
- ระบบ hook ปลั๊กอินอื่น (เช่น ItemsAdder/Oraxen) เพื่อรวม ecosystem ให้เล่นข้ามแพลตฟอร์มได้ลื่น

> เป้าหมาย: ทำให้เซิร์ฟเวอร์มีชั้น bridge ที่ "คุมเองได้" และต่อยอดเชิงแข่งขันกับโซลูชันมาตรฐานได้

## ฟีเจอร์ในเวอร์ชันนี้ (v0.4.0)

- ตรวจสถานะ Geyser/Floodgate และทำงานแบบ compatibility mode
- ระบบ static mapping จาก `config.yml`
- ระบบ plugin hooks:
  - `ItemsAdderHook`
  - `OraxenHook`
- คำสั่งแอดมิน:
  - `/nexusmc status`
  - `/nexusmc reload`
  - `/nexusmc map <java_id>`
  - `/nexusmc exportpack`
- Export Bedrock resource-pack scaffold อัตโนมัติ (`manifest.json` + `textures/item_texture.json`)
- Listener สำหรับผู้เล่น Bedrock (ผ่าน Floodgate API แบบ reflection)

## โครงสร้างหลัก

- `NexusBridgePlugin` - plugin bootstrap
- `BedrockBridgeService` - orchestration ของ mapping + hooks + reload + export
- `BridgeConfig` - parse config เป็น domain object
- `ItemMapping` - schema กลางของ item conversion
- `ItemConverterRegistry` - index java/bedrock id
- `TexturePackMapper` - map item → texture path
- `BedrockPackExporter` - pack scaffold exporter
- `GeyserSupport` - integration utility ฝั่ง Geyser/Floodgate
- `BridgePlayerListener` - bedrock-specific behavior ตอน join

## ตัวอย่าง config

```yml
bridge:
  compatibility-mode: true
  auto-detect-hooks: true
  show-mapping-stats-on-join: true

mappings:
  items:
    minecraft:diamond_sword:
      bedrock: minecraft:diamond_sword
      texture: textures/items/diamond_sword.png
      custom-model-data: 0
```

## Export pack

ใช้คำสั่ง:

```text
/nexusmc exportpack
```

ไฟล์จะถูกสร้างที่:

```text
plugins/NexusBridge/exports/bedrock-pack/
```

## Roadmap ที่ควรทำต่อ (เพื่อ "ชนะ" ในทางเทคนิค)

1. ดึง item registry แบบ dynamic จาก API ของ ItemsAdder/Oraxen จริง
2. ทำ packet-level translator สำหรับ custom model/item metadata
3. เพิ่มการคัดลอกไฟล์ texture จริงเข้า pack exporter พร้อม manifest versioning
4. เพิ่ม cache + checksum + hot-reload แบบไม่กระตุก
5. สร้าง integration tests กับ mock server + benchmark latency

## Build

```bash
mvn -q -DskipTests package
```
