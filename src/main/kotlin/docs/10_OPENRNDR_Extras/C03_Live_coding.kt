package docs.`10_OPENRNDR_Extras`

import org.openrndr.Program
import org.openrndr.application
import org.openrndr.dokgen.annotations.Code
import org.openrndr.dokgen.annotations.Text
import org.openrndr.extensions.Debug3D
import org.openrndr.extra.olive.Olive
import org.openrndr.ffmpeg.FFMPEGVideoPlayer

fun main() {
    @Text "# Live coding with orx-olive"
    @Text """By using Kotlin's ability to run script files we can build a live coding environment. The `orx-olive` library 
simplifies the work to be done to set up a live coding environment.

Start by adding `orx-live` to the `orxFeatures` set in your gradle build file .
"""

    @Code.Block
    run {
        fun main() = application {
            configure {
                width = 768
                height = 576
            }
            program {
                extend(Olive<Program>())
            }
        }
    }
    @Text """When running this script you will see a file called `live.kts` appear in `src/main/kotlin`. When you edit
this file you will see that changes are automatically detected (after save) and that the program reloads. 
"""



    @Text "## Interaction with extensions"
    @Text """The Olive extension works well together with other extensions, but only those which are installed before
the Olive extension. In the following example we see the use of `Debug3D` in combination with `Olive`."""

    @Code.Block
    run {
        fun main() = application {
            program {
                extend(Debug3D());
                extend(Olive<Program>())
            }
        }
    }

    @Text "## Adding script drag/drop support"
    @Text """A simple trick to turn your live coding host program into a versatile live coding environment is to add
file drop support. With this enabled one can drag a .kts file onto the window and drop it to load the script file.
    """.trimIndent()

    @Code.Block
    run {
        fun main() = application {
            program {
                extend(Olive<Program>()) {
                    this@program.window.drop.listen {
                        this.script = it.files.first().absolutePath
                    }
                }
            }
        }
    }

    @Text "## Adding persistent state"
    @Text """Sometimes you want to keep parts of your application persistent, that means its state will survive a script reload.
In the following example we show how you can prepare the host program to contain a persistent camera device."""

    @Code.Block
    run {
        class PersistentProgram : Program() {
            lateinit var camera: FFMPEGVideoPlayer
        }

        fun main() = application {
            program(PersistentProgram()) {
                camera = FFMPEGVideoPlayer.fromDevice()
                camera.start()

                extend(Olive<PersistentProgram>()) {
                    script = "src/main/PersistentCamera.Kt"
                }
            }
        }
    }

    @Text """Note that when you create a custom host program you also have to adjust script files to include the program
type. For example `live.kts` would become like this.
```kotlin
@file:Suppress("UNUSED_LAMBDA_EXPRESSION")
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*

{ program: PersistentProgram ->
    program.apply {
        extend {
            camera.next()
            drawer.drawStyle.colorMatrix = tint(ColorRGBa.GREEN) * grayscale(0.0, 0.0, 1.0)
            camera.draw(drawer)
        }
    }
}         
```         
"""

}